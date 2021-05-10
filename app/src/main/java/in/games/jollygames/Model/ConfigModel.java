package in.games.jollygames.Model;


public class ConfigModel {
    String id,version,app_link,share_link,message,home_text,withdraw_text,withdraw_no,tag_line,min_amount,msg_status;
    String w_saturday,w_sunday,w_amount,withdraw_limit,upi,upi_name,upi_desc,upi_type,upi_status;
    String whatsapp,call_no,share_msg;
    String terms_link,use_link,min_bet_amt,min_wallet,min_wallet_msg,starline_status,withdraw_status,add_point_status;
    String time_slot;

    public String getTime_slot() {
        return time_slot;
    }

    public void setTime_slot(String time_slot) {
        this.time_slot = time_slot;
    }

    public String getId() {
        return id;
    }

    public String getWithdraw_status() {
        return withdraw_status;
    }

    public void setWithdraw_status(String withdraw_status) {
        this.withdraw_status = withdraw_status;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getApp_link() {
        return app_link;
    }

    public void setApp_link(String app_link) {
        this.app_link = app_link;
    }

    public String getShare_link() {
        return share_link;
    }

    public void setShare_link(String share_link) {
        this.share_link = share_link;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getHome_text() {
        return home_text;
    }

    public void setHome_text(String home_text) {
        this.home_text = home_text;
    }

    public String getWithdraw_text() {
        return withdraw_text;
    }

    public void setWithdraw_text(String withdraw_text) {
        this.withdraw_text = withdraw_text;
    }

    public String getWithdraw_no() {
        return withdraw_no;
    }

    public void setWithdraw_no(String withdraw_no) {
        this.withdraw_no = withdraw_no;
    }

    public String getTag_line() {
        return tag_line;
    }

    public void setTag_line(String tag_line) {
        this.tag_line = tag_line;
    }

    public String getMin_amount() {
        return min_amount;
    }

    public void setMin_amount(String min_amount) {
        this.min_amount = min_amount;
    }

    public String getMsg_status() {
        return msg_status;
    }

    public void setMsg_status(String msg_status) {
        this.msg_status = msg_status;
    }

    public String getW_saturday() {
        return w_saturday;
    }

    public void setW_saturday(String w_saturday) {
        this.w_saturday = w_saturday;
    }

    public String getW_sunday() {
        return w_sunday;
    }

    public void setW_sunday(String w_sunday) {
        this.w_sunday = w_sunday;
    }

    public String getW_amount() {
        return w_amount;
    }

    public void setW_amount(String w_amount) {
        this.w_amount = w_amount;
    }

    public String getWithdraw_limit() {
        return withdraw_limit;
    }

    public void setWithdraw_limit(String withdraw_limit) {
        this.withdraw_limit = withdraw_limit;
    }

    public String getUpi() {
        return upi;
    }

    public void setUpi(String upi) {
        this.upi = upi;
    }

    public String getUpi_name() {
        return upi_name;
    }

    public void setUpi_name(String upi_name) {
        this.upi_name = upi_name;
    }

    public String getUpi_desc() {
        return upi_desc;
    }

    public void setUpi_desc(String upi_desc) {
        this.upi_desc = upi_desc;
    }

    public String getUpi_type() {
        return upi_type;
    }

    public void setUpi_type(String upi_type) {
        this.upi_type = upi_type;
    }

    public String getUpi_status() {
        return upi_status;
    }

    public void setUpi_status(String upi_status) {
        this.upi_status = upi_status;
    }

    public String getWhatsapp() {
        return whatsapp;
    }

    public void setWhatsapp(String whatsapp) {
        this.whatsapp = whatsapp;
    }

    public String getCall_no() {
        return call_no;
    }

    public void setCall_no(String call_no) {
        this.call_no = call_no;
    }

    public String getShare_msg() {
        return share_msg;
    }

    public void setShare_msg(String share_msg) {
        this.share_msg = share_msg;
    }

    public String getTerms_link() {
        return terms_link;
    }

    public void setTerms_link(String terms_link) {
        this.terms_link = terms_link;
    }

    public String getUse_link() {
        return use_link;
    }

    public void setUse_link(String use_link) {
        this.use_link = use_link;
    }

    public String getMin_bet_amt() {
        return min_bet_amt;
    }

    public void setMin_bet_amt(String min_bet_amt) {
        this.min_bet_amt = min_bet_amt;
    }

    public String getMin_wallet() {
        return min_wallet;
    }

    public void setMin_wallet(String min_wallet) {
        this.min_wallet = min_wallet;
    }

    public String getMin_wallet_msg() {
        return min_wallet_msg;
    }

    public void setMin_wallet_msg(String min_wallet_msg) {
        this.min_wallet_msg = min_wallet_msg;
    }

    public String getStarline_status() {
        return starline_status;
    }

    public void setStarline_status(String starline_status) {
        this.starline_status = starline_status;
    }

    public String getAdd_point_status() {
        return add_point_status;
    }

    public void setAdd_point_status(String add_point_status) {
        this.add_point_status = add_point_status;
    }

    @Override
    public String toString() {
        return "ConfigModel{" +
                "id='" + id + '\'' +
                ", version='" + version + '\'' +
                ", app_link='" + app_link + '\'' +
                ", share_link='" + share_link + '\'' +
                ", message='" + message + '\'' +
                ", home_text='" + home_text + '\'' +
                ", withdraw_text='" + withdraw_text + '\'' +
                ", withdraw_no='" + withdraw_no + '\'' +
                ", tag_line='" + tag_line + '\'' +
                ", min_amount='" + min_amount + '\'' +
                ", msg_status='" + msg_status + '\'' +
                ", w_saturday='" + w_saturday + '\'' +
                ", w_sunday='" + w_sunday + '\'' +
                ", w_amount='" + w_amount + '\'' +
                ", withdraw_limit='" + withdraw_limit + '\'' +
                ", upi='" + upi + '\'' +
                ", upi_name='" + upi_name + '\'' +
                ", upi_desc='" + upi_desc + '\'' +
                ", upi_type='" + upi_type + '\'' +
                ", upi_status='" + upi_status + '\'' +
                ", whatsapp='" + whatsapp + '\'' +
                ", call_no='" + call_no + '\'' +
                ", share_msg='" + share_msg + '\'' +
                ", terms_link='" + terms_link + '\'' +
                ", use_link='" + use_link + '\'' +
                ", min_bet_amt='" + min_bet_amt + '\'' +
                ", min_wallet='" + min_wallet + '\'' +
                ", min_wallet_msg='" + min_wallet_msg + '\'' +
                ", starline_status='" + starline_status + '\'' +
                ", withdraw_status='" + withdraw_status + '\'' +
                '}';
    }
}
