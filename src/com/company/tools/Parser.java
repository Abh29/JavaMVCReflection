package com.company.tools;

import com.company.Models.Model;

public interface Parser {

    // todo add change this to parse( Class , String ); for generalization

    Model parse(String entry , Class className) throws Exception;
}
