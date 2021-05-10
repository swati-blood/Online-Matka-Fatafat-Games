package in.games.jollygames.Model;

public class WalletObjects {

    String wallet_id,wallet_points,user_id;

    public WalletObjects() {
    }

    public WalletObjects(String wallet_id, String wallet_points, String user_id) {
        this.wallet_id = wallet_id;
        this.wallet_points = wallet_points;
        this.user_id = user_id;
    }

    public String getWallet_id() {
        return wallet_id;
    }

    public void setWallet_id(String wallet_id) {
        this.wallet_id = wallet_id;
    }

    public String getWallet_points() {
        return wallet_points;
    }

    public void setWallet_points(String wallet_points) {
        this.wallet_points = wallet_points;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }
}
