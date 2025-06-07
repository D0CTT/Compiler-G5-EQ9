package unam.fi.compilers.g5.E09.Compiler.MiniC.Lexer;

        public enum TokenType {
            INT, MAIN, WHILE, PRINT,
            ID, NUM,
            PLUS, MINUS, MUL, DIV,
            ASSIGN,
            LPAREN, RPAREN,
            LBRACE, RBRACE,
            SEMICOLON,
            GT, LT, GE, LE, EQ, NEQ,
            EOF, PRAGMA_ADDR
        }