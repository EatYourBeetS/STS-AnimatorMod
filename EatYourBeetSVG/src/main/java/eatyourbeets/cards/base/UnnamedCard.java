package eatyourbeets.cards.base;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.actions.unnamed.MoveToVoidAction;
import eatyourbeets.powers.PlayerStatistics;
import eatyourbeets.resources.GR;
import eatyourbeets.resources.unnamed.UnnamedResources;
import eatyourbeets.utilities.ColoredString;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameUtilities;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public abstract class UnnamedCard extends EYBCard
{
    protected static final Logger logger = LogManager.getLogger(UnnamedCard.class.getName());

    protected boolean isVoidbound = false;
    protected boolean isEcho = false;
    protected Color headerTextColor = null;

    public boolean enteredVoid = false;
    public int masteryCost = -2;

    protected static String Register(Class<? extends UnnamedCard> type)
    {
        return RegisterCard(type,"unnamed:" + type.getSimpleName());
    }

    private static String GetCardImage(String cardID)
    {
        String path = UnnamedResources.GetCardImage(cardID);
        if (Gdx.files.internal(path).exists())
        {
            return path;
        }
        else
        {
            return UnnamedResources.GetCardImage("unnamed:Placeholder");
        }
    }

    protected UnnamedCard(String id, int cost, CardType type, CardRarity rarity, CardTarget target)
    {
        this(id, cost, type, GR.Enums.Cards.THE_UNNAMED, rarity, target);
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
            GameActions.Bottom.Add(new MoveToVoidAction(this));
        }
    }

    protected void SetEcho(boolean value)
    {
        if (value)
        {
            if (!tags.contains(GR.Enums.CardTags.ECHO))
            {
                tags.add(GR.Enums.CardTags.ECHO);
            }
        }
        else
        {
            tags.remove(GR.Enums.CardTags.ECHO);
        }

        isEcho = value;
    }

    protected void SetVoidbound(boolean value)
    {
        if (value)
        {
            if (!tags.contains(GR.Enums.CardTags.VOIDBOUND))
            {
                tags.add(GR.Enums.CardTags.VOIDBOUND);
            }
        }
        else
        {
            tags.remove(GR.Enums.CardTags.VOIDBOUND);
        }

        isVoidbound = value;
    }

    public void SetMastery(int value)
    {
        masteryCost = value;
    }

    public boolean isEcho()
    {
        return isEcho || tags.contains(GR.Enums.CardTags.ECHO);
    }

    public boolean isVoidbound()
    {
        return isVoidbound || tags.contains(GR.Enums.CardTags.VOIDBOUND);
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
    protected ColoredString GetHeaderText()
    {
        ColoredString result = new ColoredString();
        if (isVoidbound())
        {
            result.text = "Voidbound";

            if (GameUtilities.InBattle())
            {
                if (enteredVoid)
                {
                    result.color = Settings.GOLD_COLOR.cpy();
                }
                else
                {
                    result.text = "Voidbound, Depleted";
                    result.color = new Color(1.0F, 1.0F, 1.0F, 0.7F);
                }
            }
            else
            {
                result.color = Settings.CREAM_COLOR.cpy();
            }
        }

        return result;
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

    @Override
    protected Texture GetEnergyOrb()
    {
        throw new RuntimeException("Not implemented");
    }

    @Override
    protected ColoredString GetBottomText()
    {
        return null;
    }
}