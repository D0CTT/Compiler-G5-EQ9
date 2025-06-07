package  unam.fi.compilers.g5.E09.Compiler.MiniC.Lexer;

     public class Token {
        public TokenType type;
        public String text;
        public Token(TokenType type, String text) {
            this.type = type;
            this.text = text;
        }
        public String toString() {
            return type + "(" + text + ")";
        }
    }