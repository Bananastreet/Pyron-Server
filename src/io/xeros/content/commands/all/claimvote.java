package io.xeros.content.commands.all;

import io.xeros.Configuration;
import io.xeros.model.entity.player.Player;
import io.xeros.sql.etc.HikVoteHandler;
import io.xeros.sql.etc.HiscoresUpdateQuery;
import io.xeros.sql.etc.VoteHandler;

import java.util.Optional;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class claimvote extends Commands{

    @Override
    public void execute(Player c, String commandName, String input) {
        VoteHandler updateQuery = new VoteHandler(c);
        updateQuery.run();
        //HikVoteHandler updateQuery = new HikVoteHandler(c);

        //ExecutorService executorService = Executors.newSingleThreadExecutor();
       // executorService.execute(updateQuery);
        //executorService.shutdown();
    }

    @Override
    public Optional<String> getDescription() {
        return Optional.of("Claims your votes");
    }

}
