package  unam.fi.compilers.g5.E09.Compiler.MiniC.Parser;
import  java.util.*;
import  unam.fi.compilers.g5.E09.Compiler.MiniC.Ast.*;
import unam.fi.compilers.g5.E09.Compiler.MiniC.Lexer.*;

    public class Parser {
        public Lexer lexer;
        public Token look;

        public Parser(Lexer lexer) {
            this.lexer = lexer;
            look = lexer.nextToken();
        }

        public void eat(TokenType t) {
            if (look.type == t) {
                look = lexer.nextToken();
            } else {
                throw new RuntimeException("Expected " + t + " but got " + look.type);
            }
        }

        public Program parseProgram() {
            PragmaAddr pragma = null;
            if (look.type == TokenType.PRAGMA_ADDR) {
                String text = look.text;  // e.g., "#pragma addr 6000"
                String[] parts = text.split("\\s+");
                int addr = Integer.parseInt(parts[2], 16);  // hex
                pragma = new PragmaAddr(addr);
                eat(TokenType.PRAGMA_ADDR);
            }

            eat(TokenType.INT);
            eat(TokenType.MAIN);
            eat(TokenType.LPAREN);
            eat(TokenType.RPAREN);
            eat(TokenType.LBRACE);
            List<Decl> decls = parseDecls();
            List<Stmt> stmts = parseStmts();
            eat(TokenType.RBRACE);
            return new Program(pragma, decls, stmts);
        }


        public List<Decl> parseDecls() {
            List<Decl> decls = new ArrayList<>();
            while (look.type == TokenType.INT) {
                decls.add(parseDecl());
            }
            return decls;
        }

        public Decl parseDecl() {
            eat(TokenType.INT);
            if (look.type != TokenType.ID) throw new RuntimeException("Expected ID after int");
            String id = look.text;
            eat(TokenType.ID);
            eat(TokenType.SEMICOLON);
            return new Decl(id);
        }

        public List<Stmt> parseStmts() {
            List<Stmt> stmts = new ArrayList<>();
            while (look.type == TokenType.ID || look.type == TokenType.PRINT || look.type == TokenType.WHILE) {
                stmts.add(parseStmt());
            }
            return stmts;
        }

        public Stmt parseStmt() {
            if (look.type == TokenType.ID) {
                // assignment
                String id = look.text;
                eat(TokenType.ID);
                eat(TokenType.ASSIGN);
                Expr e = parseExpr();
                eat(TokenType.SEMICOLON);
                return new Assign(id, e);
            } else if (look.type == TokenType.WHILE) {
                eat(TokenType.WHILE);
                eat(TokenType.LPAREN);
                Cond c = parseCond();
                eat(TokenType.RPAREN);
                eat(TokenType.LBRACE);
                List<Stmt> body = parseStmts();
                eat(TokenType.RBRACE);
                return new While(c, body);
            }
            throw new RuntimeException("Unexpected statement start: " + look.type);
        }

        // expr -> expr + term | expr - term | term
        public Expr parseExpr() {
            Expr left = parseTerm();
            while (look.type == TokenType.PLUS || look.type == TokenType.MINUS) {
                String op = look.text;
                if (look.type == TokenType.PLUS) eat(TokenType.PLUS);
                else eat(TokenType.MINUS);
                Expr right = parseTerm();
                left = new BinOp(left, op, right);
            }
            return left;
        }

        // term -> ID | NUM
        public Expr parseTerm() {
            if (look.type == TokenType.ID) {
                String id = look.text;
                eat(TokenType.ID);
                return new Var(id);
            } else if (look.type == TokenType.NUM) {
                int val = Integer.parseInt(look.text);
                eat(TokenType.NUM);
                return new Num(val);
            } else {
                throw new RuntimeException("Expected ID or NUM but got " + look.type);
            }
        }

        // cond -> expr comp expr
        public Cond parseCond() {
            Expr left = parseExpr();
            String op;
            switch (look.type) {
                case GT: op = ">"; eat(TokenType.GT); break;
                case LT: op = "<"; eat(TokenType.LT); break;
                case GE: op = ">="; eat(TokenType.GE); break;
                case LE: op = "<="; eat(TokenType.LE); break;
                case EQ: op = "=="; eat(TokenType.EQ); break;
                case NEQ: op = "!="; eat(TokenType.NEQ); break;
                default:
                    throw new RuntimeException("Expected relational operator but got " + look.type);
            }
            Expr right = parseExpr();
            return new Cond(left, op, right);
        }
    }