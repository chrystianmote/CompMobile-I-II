package br.com.alexromanelli.android;

public class DatabaseScripts {
    /**
     * Database creation sql statement
     */
	
    public static final String DATABASE_CREATE_TURMA = 
			"create table turma (_id integer primary key autoincrement, "
	        + "abreviacao text not null, descricao text not null,"
	        + "ano integer not null, semestre integer not null);";
    		
    public static final String DATABASE_CREATE_ALUNO = 
	        "create table aluno (_id integer primary key autoincrement, "
		    + "nome text not null, datanascimento text not null, "
		    + "sexo text(1) not null, email text not null, "
		    + "cidade text not null);";
    
    public static final String DROP_TABLE_ALUNO = "drop table if exists aluno;";

    public static final String DROP_TABLE_TURMA = "drop table if exists turma;";

}
