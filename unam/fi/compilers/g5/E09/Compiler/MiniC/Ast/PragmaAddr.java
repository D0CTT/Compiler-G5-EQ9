package unam.fi.compilers.g5.E09.Compiler.MiniC.Ast;

    public class PragmaAddr implements ASTNode {
        public int address;
        public PragmaAddr(int address) {
            this.address = address;
        }
    }