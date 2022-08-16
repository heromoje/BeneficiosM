package bcp.bo.service.model.response;

import org.codehaus.jackson.annotate.JsonProperty;

import java.io.Serializable;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Locale;

/**
 * Created by BC2078 on 10/17/2015.
 */
public class Promotion implements Serializable {
    public static final String INTENT_FILTER_DISCOUNT_IMAGE_LIST_REFRESH = "DISCOUNT_IMAGE_LIST_REFRESH";
    public static final String INTENT_FILTER_DISCOUNT_IMAGE_DETAIL_REFRESH = "DISCOUNT_IMAGE_DETAIL_REFRESH";
    public static final String KEY_PRODUCT_ID_IMAGE_REFRESH = "KEY_PRODUCT_ID";

    public final static Locale LOCALE= Locale.ENGLISH;
    public final static String STATE_ACTIVE="A";
    public final static String STATE_USED="U";
    public final static String STATE_CONTINGENCY="C";
    public final static String STATE_EXPIRED="V";

    @JsonProperty("idPromotion")
    public int Id;
    @JsonProperty("namePromotion")
    public String NamePromotion;
    @JsonProperty("messagePromotion")
    public String MessagePromotion;
    @JsonProperty("descriptionPromotion")
    public String DescriptionPromotion;
    @JsonProperty("legendPromotion")
    public String LegendPromotion;
    @JsonProperty("discountCommerce")
    public void setDiscountCommerce(String discountCommerce){
        try {
            DecimalFormat decimalFormat=(DecimalFormat) NumberFormat.getInstance(LOCALE);
            decimalFormat.setParseBigDecimal(true);
            DiscountCommerce=(BigDecimal)decimalFormat.parseObject(discountCommerce);
        } catch (ParseException e) {
            DiscountCommerce=BigDecimal.ZERO;
        }
    }
    public BigDecimal DiscountCommerce;
    @JsonProperty("discountBcp")
    public void setDiscountBCP(String discountBCP){
        try {
            DecimalFormat decimalFormat=(DecimalFormat) NumberFormat.getInstance(LOCALE);
            decimalFormat.setParseBigDecimal(true);
            DiscountBCP=(BigDecimal)decimalFormat.parseObject(discountBCP);
        } catch (ParseException e) {
            DiscountBCP=BigDecimal.ZERO;
        }
    }
    public BigDecimal DiscountBCP;
    @JsonProperty("amountMax")
    public double AmountMax;

    public String ImageListURL;
    public String ImageTicketURL;

    @JsonProperty("imageURL")
    public void setImageURL(String imageURL){
        ImageURL = imageURL;
        String[] urls=imageURL.split("\\|");
        if(urls.length>=2){
            //ImageListURL=urls[0].replace("deviis10","172.31.164.169");
            //ImageTicketURL=urls[1].replace("deviis10","172.31.164.169");
            ImageListURL=urls[0];
            ImageTicketURL=urls[1];
        }
        else{
            ImageListURL="";
            ImageTicketURL="";
        }
        ImageURL=imageURL;
    }
    public String ImageURL;
    @JsonProperty("dateStart")
    public String DateStart;
    @JsonProperty("dateExpiration")
    public String DateExpiration;
    @JsonProperty("numberTicket")
    public String NumberTicket;
    @JsonProperty("stateTicket")
    public String StateTicket;
    @JsonProperty("ratingTicket")
    public short RatingTicket;

    private Commerce _commerce;

    public Commerce getCommerce(){
        return _commerce;
    }
    public void setCommerce(Commerce commerce){
        _commerce=commerce;
    }

    public boolean isActive(){
        return StateTicket.equals(STATE_ACTIVE);
    }
    public boolean isUsed(){
        return StateTicket.equals(STATE_USED) | StateTicket.equals(STATE_CONTINGENCY);
    }
    public boolean isExpired(){
        return StateTicket.equals(STATE_EXPIRED);
    }

    public static Promotion init(){
        Promotion promotion=new Promotion();
        promotion.Id=1000;
        promotion.NamePromotion="";
        promotion.MessagePromotion="";
        promotion.DescriptionPromotion="Descuento 35%";
        promotion.LegendPromotion="Descuento";
        promotion.DiscountCommerce=BigDecimal.TEN;
        promotion.DiscountBCP=BigDecimal.TEN;
        promotion.AmountMax=100;
        promotion.ImageURL="http://www.google.com";
        promotion.DateStart="19-10-2015";
        promotion.DateExpiration="19-11-2015";
        promotion.NumberTicket="AB2XK0S";
        promotion.StateTicket="A";
        promotion.RatingTicket=4;

        Commerce commerce=new Commerce();
        commerce.Category="Diversion";
        commerce.Name="BURGER KING";
        commerce.Contacts=new ArrayList<>();
        promotion.setCommerce(commerce);

        return promotion;
    }

    public static class PromotionComparator implements Comparator<Promotion> {
        @Override
        public int compare(Promotion a, Promotion b) {
            if(a.isActive() && b.isUsed()){
                return -1;
            }
            else if(a.isActive() && b.isExpired()){
                return -1;
            }
            else if(a.isUsed() && b.isActive()){
                return 1;
            }
            else if(a.isUsed() && b.isExpired()){
                return -1;
            }
            else if(a.isExpired() && b.isActive()){
                return 1;
            }
            else if(a.isExpired() && b.isUsed()){
                return 1;
            }
            return 0;
        }
    }
}