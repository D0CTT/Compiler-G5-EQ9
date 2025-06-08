
package  unam.fi.compilers.g5.E09.Compiler.MiniC.CodeGen;
import  java.io.*;
import  java.util.*;
import  unam.fi.compilers.g5.E09.Compiler.MiniC.Ast.*;

public class CodeGen {
    public Program program;
    public PrintWriter out;
    public Map<String, Integer> vars = new LinkedHashMap<>();
    public int labelCount = 0;
    public int memPointer = 0x6000;  // Dirección inicial para variables

    public CodeGen(Program program, PrintWriter out) {
        this.program = program;
        this.out = out;
        this.memPointer = (program.pragma != null) ? program.pragma.address : 0x6000;
    }

    public void generate() {
        // Cabeceras requeridas
        out.println("CPU \"Z80.tbl\"");
        out.println("HOF \"INT8\"");
        out.println();
        
        out.println("; Variables asignadas desde " + String.format("%04xh", program.pragma.address));
        for (Decl d : program.decls) {
            vars.put(d.id, memPointer++);
        }
        out.println();

        out.println("START:");
        for (Stmt s : program.stmts) {
            genStmt(s);
        }
        out.println("HALT");
    }

    public void genStmt(Stmt s) {
        if (s instanceof Assign a) {
            genExpr(a.expr); // Resultado en A
            int addr = vars.get(a.id);
            out.println(String.format("    LD (%04Xh), A", addr));
        } else if (s instanceof While w) {
            int startLabel = labelCount++;
            int endLabel = labelCount++;
            out.println("L" + startLabel + ":");
            genCondJump(w.cond, "L" + endLabel, true);
            for (Stmt st : w.body) {
                genStmt(st);
            }
            out.println("    JP L" + startLabel);
            out.println("L" + endLabel + ":");
        } else {
            throw new RuntimeException("Unknown statement type");
        }
    }

    public void genExpr(Expr e) {
        if (e instanceof Num n) {
            out.println("    LD A, " + n.value);
        } else if (e instanceof Var v) {
            int addr = vars.get(v.id);
            out.println(String.format("    LD A, (%04Xh)", addr));
        } else if (e instanceof BinOp b) {
            genExpr(b.left);       // A = left
            out.println("    LD B, A");
            genExpr(b.right);      // A = right
            out.println("    LD C, A");
            if (b.op.equals("+")) {
                out.println("    LD A, B");
                out.println("    ADD A, C");
            } else if (b.op.equals("-")) {
                out.println("    LD A, B");
                out.println("    SUB C");
            } else {
                throw new RuntimeException("Unsupported operator: " + b.op);
            }
        } else {
            throw new RuntimeException("Unknown expression type");
        }
    }

    public void genCondJump(Cond c, String label, boolean jumpIfTrue) {
        genExpr(c.left);
        out.println("    LD B, A");
        genExpr(c.right);
        out.println("    CP B");

        if (jumpIfTrue) {
            switch (c.op) {
                case "==":
                    out.println("    JP NZ, " + label); // z != A → salto
                    break;
                case "!=":
                    out.println("    JP Z, " + label);  // z == A → salto
                    break;
                case ">":
                    out.println("    JP NC, " + label); // z <= A → salto
                    out.println("    JP Z, " + label);  // z == A → salto
                    break;
                case "<":
                    out.println("    JP C, " + label);  // z > A → no carry → salto
                    out.println("    JP Z, " + label);  // z == A → también salto
                    break;
                case ">=":
                    int ge = labelCount++;
                    out.println("    JP Z, _ok" + ge);   // z == A
                    out.println("    JP C, _ok" + ge);   // z > A
                    out.println("    JP " + label);      // z < A → salto
                    out.println("_ok" + ge + ":");
                    break;
                case "<=":
                    out.println("    JP C, " + label);  // Salta si z > A
                    break;
                default:
                    throw new RuntimeException("Unknown comparison operator: " + c.op);
            }

        } else {
        // invertir lógica de salto
            switch (c.op) {
                case "==":
                    out.println("    JP NZ, " + label);
                    break;
                case "!=":
                    out.println("    JP Z, " + label);
                    break;
                case ">":
                    out.println("    JP Z, " + label);
                    out.println("    JP C, " + label);
                    break;
                case "<":
                    out.println("    JP Z, " + label);
                    out.println("    JP NC, " + label);
                    break;
                case ">=":
                    out.println("    JP C, " + label);
                    break;
                case "<=":
                    out.println("    JP NC, " + label);
                    break;
                default:
                    throw new RuntimeException("Unknown comparison operator: " + c.op);
            }
        }
    }
}
