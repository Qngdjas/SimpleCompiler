package ru.qngdjas.study.compiler.backend.generator;

import ru.qngdjas.study.compiler.frontend.parser.parsetree.program.ProgramParseTree;
import ru.qngdjas.study.compiler.frontend.parser.parsetree.program.code.*;
import ru.qngdjas.study.compiler.frontend.parser.parsetree.program.code.expression.ExpressionParseTree;
import ru.qngdjas.study.compiler.frontend.parser.parsetree.program.code.expression.ValueParseTree;
import ru.qngdjas.study.compiler.frontend.parser.parsetree.program.var.VarParseTree;
import ru.qngdjas.study.compiler.frontend.parser.parsetree.program.var.VarsParseTree;

public interface ParserTreeVisitor {

    String visitProgramParseTree(ProgramParseTree programPT);

    String visitVarsParseTree(VarsParseTree varsPT);

    String visitVarParseTree(VarParseTree varPT);

    String visitCodeParseTree(CodeParseTree codePT);

    String visitAssignmentStatementParseTree(AssignmentStatementParseTree aStatementPT);

    String visitIfStatementParseTree(IfStatementParseTree aStatementPT);

    String visitWhileStatementParseTree(WhileStatementParseTree aStatementPT);

    String visitPrintStatementParseTree(PrintStatementParseTree aStatementPT);

    String visitExpressionParseTree(ExpressionParseTree expressionPT);

    String visitValueParseTree(ValueParseTree expressionPT);

}
