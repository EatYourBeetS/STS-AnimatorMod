package eatyourbeets.cards;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.actions.utility.SFXAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.powers.PlayerStatistics;
import eatyourbeets.resources.Resources_Unnamed;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameActionsHelper_Legacy;
import eatyourbeets.utilities.GameUtilities;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import patches.AbstractEnums;

public abstract class UnnamedCard extends EYBCard
{
    protected static final Logger logger = LogManager.getLogger(UnnamedCard.class.getName());

    protected boolean isVoidbound = false;
    protected boolean isEcho = false;
    protected Color headerTextColor = null;

    public boolean enteredVoid = false;
    public int masteryCost = -2;

    protected static String Register(String cardID, EYBCardBadge... badges)
    {
        return RegisterCard("unnamed:" + cardID, badges);
    }

    private static String GetCardImage(String cardID)
    {
        String path = Resources_Unnamed.GetCardImage(cardID);
        if (Gdx.files.internal(path).exists())
        {
            return path;
        }
        else
        {
            return Resources_Unnamed.GetCardImage("unnamed:Placeholder");
        }
    }

    protected UnnamedCard(String id, int cost, CardType type, CardRarity rarity, CardTarget target)
    {
        this(id, cost, type, AbstractEnums.Cards.THE_UNNAMED, rarity, target);
    }

    protected UnnamedCard(String id, int cost, CardType type, CardColor color, CardRarity rarity, CardTarget target)
    {
        this(staticCardData.get(id), id, GetCardImage(id), cost, type, color, rarity, target);
    }

    protected UnnamedCard(EYBCardData data, String id, String imagePath, int cost, CardType type, CardColor color, CardRarity rarity, CardTarget target)
    {
        super(data, id, imagePath, cost, type, color, rarity, target);
    }

    @Override
    public void triggerOnExhaust()
    {
        super.triggerOnExhaust();

        if (isVoidbound)
        {
            GameActionsHelper_Legacy.MoveToVoid(this);
        }
    }

    protected void SetEcho(boolean value)
    {
        if (value)
        {
            if (!tags.contains(AbstractEnums.CardTags.ECHO))
            {
                tags.add(AbstractEnums.CardTags.ECHO);
            }
        }
        else
        {
            tags.remove(AbstractEnums.CardTags.ECHO);
        }

        isEcho = value;
    }

    protected void SetVoidbound(boolean value)
    {
        if (value)
        {
            if (!tags.contains(AbstractEnums.CardTags.VOIDBOUND))
            {
                tags.add(AbstractEnums.CardTags.VOIDBOUND);
            }
        }
        else
        {
            tags.remove(AbstractEnums.CardTags.VOIDBOUND);
        }

        isVoidbound = value;
    }

    public void SetMastery(int value)
    {
        masteryCost = value;
    }

    public boolean isEcho()
    {
        return isEcho || tags.contains(AbstractEnums.CardTags.ECHO);
    }

    public boolean isVoidbound()
    {
        return isVoidbound || tags.contains(AbstractEnums.CardTags.VOIDBOUND);
    }

    public boolean isDepleted()
    {
        return isVoidbound && !enteredVoid;
    }

    public void OnEnteredVoid()
    {
        if (!enteredVoid)
        {
            GameActions.Bottom.SFX("ANIMATOR_MEGUMIN_CHARGE", 0.1F);
            enteredVoid = true;
        }
    }

    @Override
    public AbstractCard makeStatEquivalentCopy()
    {
        UnnamedCard copy = (UnnamedCard) super.makeStatEquivalentCopy();

        copy.enteredVoid = enteredVoid;
        copy.masteryCost = masteryCost;

        return copy;
    }

    @Override
    public boolean cardPlayable(AbstractMonster m)
    {
        if (!PlayerStatistics.Void.CanUse(this))
        {
            this.cantUseMessage = TEXT[11];
            return false;
        }

        return super.cardPlayable(m);
    }

    @Override
    protected String GetHeaderText()
    {
        String text = null;
        if (isVoidbound())
        {
            text = "Voidbound";

            if (GameUtilities.InBattle())
            {
                if (enteredVoid)
                {
                    headerTextColor = Settings.GOLD_COLOR.cpy();
                }
                else
                {
                    text = "Voidbound, Depleted";
                    headerTextColor = new Color(1.0F, 1.0F, 1.0F, 0.7F);
                }
            }
            else
            {
                headerTextColor = super.GetHeaderColor();
            }
        }

        return text;
    }

    @Override
    protected Color GetHeaderColor()
    {
        return headerTextColor;
    }

    public String getMasteryCostString()
    {
        if (masteryCost >= 0)
        {
            return String.valueOf(masteryCost);
        }
        else if (masteryCost == -1)
        {
            return "Y";
        }
        else
        {
            return "";
        }
    }
}