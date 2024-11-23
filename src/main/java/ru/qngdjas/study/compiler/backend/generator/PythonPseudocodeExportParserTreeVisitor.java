package ru.qngdjas.study.compiler.backend.generator;

import ru.qngdjas.study.compiler.frontend.parser.parsetree.program.ProgramParseTree;
import ru.qngdjas.study.compiler.frontend.parser.parsetree.program.code.*;
import ru.qngdjas.study.compiler.frontend.parser.parsetree.program.code.expression.ExpressionParseTree;
import ru.qngdjas.study.compiler.frontend.parser.parsetree.program.code.expression.ValueParseTree;
import ru.qngdjas.study.compiler.frontend.parser.parsetree.program.var.VarParseTree;
import ru.qngdjas.study.compiler.frontend.parser.parsetree.program.var.VarsParseTree;

import java.util.List;

public class PythonPseudocodeExportParserTreeVisitor implements ParserTreeVisitor {

    private int gap = 0;

    @Override
    public String visitProgramParseTree(ProgramParseTree programPT) {
        StringBuilder builder = new StringBuilder();
        builder.append("class ");
        builder.append(programPT.getName().getValue());
        builder.append(":\n\n");
        gap++;
        builder.append(programPT.getVarPT().accept(this));
        builder.append("\n");
        builder.append(" ".repeat(gap * 4));
        builder.append("def __call__(self):\n");
        builder.append(programPT.getCodePT().accept(this));
        gap--;
        return builder.toString();
    }

    @Override
    public String visitVarsParseTree(VarsParseTree varsPT) {
        StringBuilder builder = new StringBuilder();
        builder.append(" ".repeat(gap * 4));
        builder.append("def __init__(self");
        List<String> vars = varsPT.getVars().stream().map(var -> var.accept(this)).toList();
        for (String var : vars) {
            builder.append(", ");
            builder.append(var);
        }
        gap++;
        builder.append("):\n");
        for (String var : vars) {
            builder.append(" ".repeat(gap * 4));
            builder.append("self.");
            builder.append(var);
            builder.append(" = ");
            builder.append(var);
            builder.append("\n");
        }
        gap--;
        return builder.toString();
    }

    @Override
    public String visitVarParseTree(VarParseTree varPT) {
        return varPT.getId().getValue();
    }

    @Override
    public String visitCodeParseTree(CodeParseTree codePT) {
        StringBuilder builder = new StringBuilder();
        gap++;
        for (StatementParseTree statement : codePT.getStatements()) {
            builder.append(statement.accept(this));
            builder.append("\n");
        }
        gap--;
        return builder.toString();
    }

    @Override
    public String visitAssignmentStatementParseTree(AssignmentStatementParseTree aStatementPT) {
        StringBuilder builder = new StringBuilder();
        builder.append(" ".repeat(gap * 4));
        builder.append(aStatementPT.getName().getValue());
        builder.append(" = ");
        builder.append(aStatementPT.getExpressionPT().accept(this));
        return builder.toString();
    }

    @Override
    public String visitIfStatementParseTree(IfStatementParseTree iStatementPT) {
        StringBuilder builder = new StringBuilder();
        builder.append(" ".repeat(gap * 4));
        builder.append("if ");
        builder.append(iStatementPT.getExpressionPT().accept(this));
        builder.append(":\n");
        builder.append(iStatementPT.getThenPT().accept(this));
        if (iStatementPT.getElsePT() != null) {
            builder.append(" ".repeat(gap * 4));
            builder.append("else:\n");
            builder.append(iStatementPT.getElsePT().accept(this));
        }
        return builder.toString();
    }

    @Override
    public String visitWhileStatementParseTree(WhileStatementParseTree wStatementPT) {
        StringBuilder builder = new StringBuilder();
        builder.append(" ".repeat(gap * 4));
        builder.append("while ");
        builder.append(wStatementPT.getExpressionPT().accept(this));
        builder.append(":\n");
        builder.append(wStatementPT.getDoPT().accept(this));
        return builder.toString();
    }

    @Override
    public String visitPrintStatementParseTree(PrintStatementParseTree pStatementPT) {
        StringBuilder builder = new StringBuilder();
        builder.append(" ".repeat(gap * 4));
        builder.append("print(");
        builder.append(pStatementPT.getExpressionPT().accept(this));
        builder.append(")");
        return builder.toString();
    }

    @Override
    public String visitExpressionParseTree(ExpressionParseTree expressionPT) {
        StringBuilder builder = new StringBuilder();
        builder.append(expressionPT.left().accept(this));
        builder.append(" ");
        if (!expressionPT.operator().getValue().equals("=")) {
            builder.append(expressionPT.operator().getValue());
        } else {
            builder.append("==");
        }
        builder.append(" ");
        builder.append(expressionPT.right().accept(this));
        return builder.toString();
    }

    @Override
    public String visitValueParseTree(ValueParseTree expressionPT) {
        return expressionPT.value().getValue();
    }
}
