
package beans;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

public class TrelloBoard {

    @SerializedName("id")
    @Expose
    public String id;
    @SerializedName("name")
    @Expose
    public String name;
    @SerializedName("desc")
    @Expose
    public String desc;
    @SerializedName("descData")
    @Expose
    public Object descData;
    @SerializedName("closed")
    @Expose
    public Boolean closed;
    @SerializedName("idOrganization")
    @Expose
    public Object idOrganization;
    @SerializedName("pinned")
    @Expose
    public Boolean pinned;
    @SerializedName("url")
    @Expose
    public String url;
    @SerializedName("shortUrl")
    @Expose
    public String shortUrl;
    @SerializedName("prefs")
    @Expose
    public Prefs prefs;
    @SerializedName("labelNames")
    @Expose
    public LabelNames labelNames;

    @Override
    public String toString() {
        return new ToStringBuilder(this).append("id", id).append("name", name).append("desc", desc).append("descData", descData).append("closed", closed).append("idOrganization", idOrganization).append("pinned", pinned).append("url", url).append("shortUrl", shortUrl).append("prefs", prefs).append("labelNames", labelNames).toString();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(descData).append(pinned).append(labelNames).append(shortUrl).append(name).append(idOrganization).append(closed).append(id).append(url).append(desc).append(prefs).toHashCode();
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof TrelloBoard) == false) {
            return false;
        }
        TrelloBoard rhs = ((TrelloBoard) other);
        return new EqualsBuilder().append(descData, rhs.descData).append(pinned, rhs.pinned).append(labelNames, rhs.labelNames).append(shortUrl, rhs.shortUrl).append(name, rhs.name).append(idOrganization, rhs.idOrganization).append(closed, rhs.closed).append(id, rhs.id).append(url, rhs.url).append(desc, rhs.desc).append(prefs, rhs.prefs).isEquals();
    }

}
