package utilities;

public class ScenarioFail {
    private static final ThreadLocal<Boolean> FAILED =
            ThreadLocal.withInitial(() -> false);
    private static final ThreadLocal<StringBuilder> MSG =
            ThreadLocal.withInitial(StringBuilder::new);

    public static void mark(String m){
        FAILED.set(true);
        MSG.get().append(m).append('\n');
    }
    public static boolean any(){ return FAILED.get(); }
    public static String msg(){ return MSG.get().toString(); }
    public static void clear(){ FAILED.remove(); MSG.remove(); }
}
