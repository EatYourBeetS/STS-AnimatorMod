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
import eatyourbeets.resources.unnamed.UnnamedImages;
import eatyourbeets.utilities.ColoredString;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameUtilities;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public abstract class UnnamedCard extends EYBCard
{
    protected static final UnnamedImages IMAGES = GR.Unnamed.Images;
    protected static final Logger logger = LogManager.getLogger(UnnamedCard.class.getName());

    protected boolean isVoidbound = false;
    protected boolean isEcho = false;
    protected Color headerTextColor = null;

    public boolean enteredVoid = false;
    public int masteryCost = -2;

    protected static EYBCardData Register(Class<? extends UnnamedCard> type)
    {
        EYBCardData data = RegisterCardData(type, GR.Unnamed.CreateID(type.getSimpleName())).SetColor(GR.Unnamed.CardColor);
        if (!Gdx.files.internal(data.ImagePath).exists())
        {
            data.ImagePath = GR.GetCardImage("unnamed:Placeholder");
        }

        return data;
    }

    protected UnnamedCard(EYBCardData cardData)
    {
        this(cardData, cardData.ID, cardData.ImagePath, cardData.BaseCost, cardData.CardType, cardData.CardColor, cardData.CardRarity, cardData.CardTarget.ToCardTarget());
    }

    protected UnnamedCard(EYBCardData data, String id, String imagePath, int cost, CardType type, CardColor color, CardRarity rarity, CardTarget target)
    {
        super(data, id, imagePath, cost, type, color, rarity, target);

        cropPortrait = false;
        SetMultiDamage(data.CardTarget == EYBCardTarget.ALL);
        SetAttackTarget(data.CardTarget);
        SetAttackType(data.AttackType);
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
    public ColoredString GetHeaderText()
    {
        ColoredString result = null;
        if (isVoidbound())
        {
            result = new ColoredString();
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
    public ColoredString GetBottomText()
    {
        return null;
    }

    @Override
    protected Texture GetCardBackground()
    {
        if (color == GR.Unnamed.CardColor)
        {
            switch (type)
            {
                case ATTACK: return GR.Unnamed.Images.CARD_BACKGROUND_ATTACK.Texture();
                case POWER: return GR.Unnamed.Images.CARD_BACKGROUND_POWER.Texture();
                default: return GR.Unnamed.Images.CARD_BACKGROUND_SKILL.Texture();
            }
        }
        else if (color == CardColor.COLORLESS)
        {
            switch (type)
            {
                case ATTACK: return GR.Animator.Images.CARD_BACKGROUND_ATTACK_UR.Texture();
                case POWER: return GR.Animator.Images.CARD_BACKGROUND_POWER_UR.Texture();
                default: return GR.Animator.Images.CARD_BACKGROUND_SKILL_UR.Texture();
            }
        }

        return super.GetCardBackground();
    }

    @Override
    protected Texture GetEnergyOrb()
    {
        if (color == GR.Unnamed.CardColor)
        {
            return IMAGES.CARD_ENERGY_ORB_A.Texture();
        }

        return null;
    }

    @Override
    protected Texture GetCardBanner()
    {
        if (rarity == CardRarity.SPECIAL)
        {
            return IMAGES.CARD_BANNER_SPECIAL.Texture();
        }

        return null;
    }

    @Override
    protected Texture GetPortraitFrame()
    {
        if (rarity == CardRarity.SPECIAL)
        {
            switch (type)
            {
                case ATTACK: return IMAGES.CARD_FRAME_ATTACK_SPECIAL.Texture();
                case POWER: return IMAGES.CARD_FRAME_POWER_SPECIAL.Texture();
                default: return IMAGES.CARD_FRAME_SKILL_SPECIAL.Texture();
            }
        }

        return null;
    }
}