
    package unam.fi.compilers.g5.E09.Compiler.MiniC.Ast;
    import java.util.List;
    
    public class Program implements ASTNode {
        public PragmaAddr pragma;
        public List<Decl> decls;
        public List<Stmt> stmts;
        public Program(PragmaAddr pragma, List<Decl> decls, List<Stmt> stmts) {
            this.pragma = pragma;
            this.decls = decls;
            this.stmts = stmts;
        }
    }