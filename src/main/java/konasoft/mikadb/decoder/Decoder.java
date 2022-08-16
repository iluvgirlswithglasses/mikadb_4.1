package konasoft.mikadb.decoder;

public abstract class Decoder {
    public int indexOf(String[] arr, String str) {
        for (int i = 0; i < arr.length; i++) {
            if (arr[i].equals(str)) return i;
        }
        return -1;
    }

    public String get(String[] arr, int i) {
        if (i == -1) return "Unknown";
        return arr[i];
    }
}
