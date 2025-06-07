package  unam.fi.compilers.g5.E09.Compiler.MiniC.Lexer;


public class Lexer {
        String input;
        int pos = 0;
        char ch;

        public Lexer(String input) {
            this.input = input;
            ch = input.length() > 0 ? input.charAt(0) : '\0';
        }

        public void nextChar() {
            pos++;
            ch = pos < input.length() ? input.charAt(pos) : '\0';
        }

        public boolean isWhitespace(char c) {
            return c==' '||c=='\t'||c=='\n'||c=='\r';
        }

        public boolean isLetter(char c) {
            return (c >= 'a' && c <= 'z') || (c >= 'A' && c <= 'Z') || c == '_';
        }

        public boolean isDigit(char c) {
            return c >= '0' && c <= '9';
        }

        public void skipWhitespace() {
            while (isWhitespace(ch)) nextChar();
        }

        public Token nextToken() {
            skipWhitespace();
            if (ch == '\0') return new Token(TokenType.EOF, "");

            // --- DIRECTIVAS #pragma ---
            if (ch == '#') {
                int start = pos;
                while (ch != '\0' && ch != ';') nextChar();
                String directive = input.substring(start, pos);
                nextChar(); // consume ';'
                if (directive.startsWith("#pragma addr")) {
                    return new Token(TokenType.PRAGMA_ADDR, directive.trim());
                }
                throw new RuntimeException("Unknown pragma directive: " + directive);
            }
            // Keywords & identifiers
            if (isLetter(ch)) {
                int start = pos;
                while (isLetter(ch) || isDigit(ch)) nextChar();
                String word = input.substring(start, pos);
                switch(word) {
                    case "int": return new Token(TokenType.INT, word);
                    case "main": return new Token(TokenType.MAIN, word);
                    case "while": return new Token(TokenType.WHILE, word);
                    case "print": return new Token(TokenType.PRINT, word);
                    default: return new Token(TokenType.ID, word);
                }
            }

            // Numbers
            if (isDigit(ch)) {
                int start = pos;
                while (isDigit(ch)) nextChar();
                String num = input.substring(start, pos);
                return new Token(TokenType.NUM, num);
            }

            // Operators and punctuation
            switch(ch) {
                case '+': nextChar(); return new Token(TokenType.PLUS, "+");
                case '-': nextChar(); return new Token(TokenType.MINUS, "-");
                case '*': nextChar(); return new Token(TokenType.MUL, "*");
                case '/': nextChar(); return new Token(TokenType.DIV, "/");
                case '=':
                    nextChar();
                    if (ch == '=') { nextChar(); return new Token(TokenType.EQ, "=="); }
                    else return new Token(TokenType.ASSIGN, "=");
                case '>':
                    nextChar();
                    if (ch == '=') { nextChar(); return new Token(TokenType.GE, ">="); }
                    else return new Token(TokenType.GT, ">");
                case '<':
                    nextChar();
                    if (ch == '=') { nextChar(); return new Token(TokenType.LE, "<="); }
                    else return new Token(TokenType.LT, "<");
                case '!':
                    nextChar();
                    if (ch == '=') { nextChar(); return new Token(TokenType.NEQ, "!="); }
                    throw new RuntimeException("Unexpected character after '!': " + ch);
                case '(':
                    nextChar(); return new Token(TokenType.LPAREN, "(");
                case ')':
                    nextChar(); return new Token(TokenType.RPAREN, ")");
                case '{':
                    nextChar(); return new Token(TokenType.LBRACE, "{");
                case '}':
                    nextChar(); return new Token(TokenType.RBRACE, "}");
                case ';':
                    nextChar(); return new Token(TokenType.SEMICOLON, ";");
                default:
                    throw new RuntimeException("Unexpected character: " + ch);
            }
        }
    }