package com.dbsoftware.bungeeutilisals.api;

import java.util.List;

import com.google.common.collect.Lists;

public class CommandAPI {
	
	static CommandAPI instance = new CommandAPI();
	public List<DBCommand> commands = Lists.newArrayList();
	
	public static CommandAPI getInstance(){
		return instance;
	}
	
	public void registerCommand(DBCommand command){
		command.register();
		commands.add(command);
	}
}