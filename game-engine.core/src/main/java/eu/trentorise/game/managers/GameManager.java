package eu.trentorise.game.managers;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import eu.trentorise.game.core.AppContextProvider;
import eu.trentorise.game.core.GameContext;
import eu.trentorise.game.core.GameTask;
import eu.trentorise.game.model.ClasspathRule;
import eu.trentorise.game.model.DBRule;
import eu.trentorise.game.model.FSRule;
import eu.trentorise.game.model.Game;
import eu.trentorise.game.model.Rule;
import eu.trentorise.game.repo.GamePersistence;
import eu.trentorise.game.repo.GameRepo;
import eu.trentorise.game.repo.RuleRepo;
import eu.trentorise.game.services.GameService;
import eu.trentorise.game.services.TaskService;

@Component
public class GameManager implements GameService {

	private final Logger logger = LoggerFactory.getLogger(GameManager.class);

	@Autowired
	TaskService taskSrv;

	@Autowired
	AppContextProvider provider;

	@Autowired
	GameRepo gameRepo;

	@Autowired
	RuleRepo ruleRepo;

	@PostConstruct
	@SuppressWarnings("unused")
	private void startup() {
		for (Game game : loadGames(true)) {
			startupTasks(game.getId());
		}
	}

	public String getGameIdByAction(String actionId) {
		GamePersistence game = gameRepo.findByActions(actionId);
		return game != null ? game.getId() : null;
	}

	public void startupTasks(String gameId) {
		Game game = loadGameDefinitionById(gameId);
		if (game != null) {
			for (GameTask task : game.getTasks()) {
				taskSrv.createTask(
						task,
						(GameContext) provider.getApplicationContext().getBean(
								"gameCtx", gameId, task));
			}
		}

	}

	public void saveGameDefinition(Game game) {
		gameRepo.save(new GamePersistence(game));
	}

	public Game loadGameDefinitionById(String gameId) {
		GamePersistence gp = gameRepo.findOne(gameId);
		return gp == null ? null : gp.toGame();
	}

	public List<Game> loadGames(boolean onlyActive) {
		List<Game> result = new ArrayList<Game>();
		for (GamePersistence gp : gameRepo.findByTerminated(!onlyActive)) {
			result.add(gp.toGame());
		}
		return result;
	}

	public List<Game> loadAllGames() {
		List<Game> result = new ArrayList<Game>();
		for (GamePersistence gp : gameRepo.findAll()) {
			result.add(gp.toGame());
		}
		return result;
	}

	public void addRule(Rule rule) {
		if (rule != null) {
			Game game = loadGameDefinitionById(rule.getGameId());
			if (game != null) {
				if (rule instanceof ClasspathRule) {
					game.getRules().add(
							"classpath://" + ((ClasspathRule) rule).getUrl());
				}

				if (rule instanceof FSRule) {
					game.getRules().add("file://" + ((FSRule) rule).getUrl());
				}

				if (rule instanceof DBRule) {
					rule = ruleRepo.save((DBRule) rule);
					game.getRules().add("db://" + ((DBRule) rule).getId());
				}

				saveGameDefinition(game);
			} else {
				logger.error("Game {} not found", rule.getGameId());
			}
		}
	}

	public Rule loadRule(String gameId, String url) {
		Rule rule = null;
		if (url != null) {
			if (url.startsWith("db://")) {
				url = url.substring("db://".length());
				return ruleRepo.findOne(url);
			} else if (url.startsWith("classpath://")) {
				url = url.substring("classpath://".length());
				if (Thread.currentThread().getContextClassLoader()
						.getResource(url) != null) {
					return new ClasspathRule(gameId, url);
				}

			} else if (url.startsWith("file://")) {
				url = url.substring("file://".length());
				if (new File(url).exists()) {
					return new FSRule(gameId, url);
				}
			}
		}
		return rule;
	}

	@Scheduled(cron = "0 * * * * *")
	public void taskDestroyer() {
		logger.info("task destroyer invocation");
		long deadline = System.currentTimeMillis();

		List<Game> games = loadGames(true);
		for (Game game : games) {
			if (game.getExpiration() > 0 && game.getExpiration() < deadline) {
				for (GameTask task : game.getTasks()) {
					if (taskSrv.destroyTask(task, game.getId())) {
						logger.info("Destroy task - {} - of game {}",
								task.getName(), game.getId());
					}
				}
				game.setTerminated(true);
				saveGameDefinition(game);
			}
		}

	}

	public Game loadGameDefinitionByAction(String actionId) {
		GamePersistence gp = gameRepo.findByActions(actionId);
		return gp != null ? gp.toGame() : null;
	}
}