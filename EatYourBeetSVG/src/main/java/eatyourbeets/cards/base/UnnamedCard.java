package eatyourbeets.cards.base;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.monsters.PlayerMinions.UnnamedDoll;
import eatyourbeets.powers.CombatStats;
import eatyourbeets.powers.unnamed.SummoningSicknessPower;
import eatyourbeets.resources.GR;
import eatyourbeets.resources.unnamed.UnnamedImages;
import eatyourbeets.utilities.*;

import java.util.ArrayList;

public abstract class UnnamedCard extends EYBCard
{
    protected static final Color defaultGlowColor = AbstractCard.BLUE_BORDER_GLOW_COLOR;
    protected static final AbstractPlayer.PlayerClass PLAYER_CLASS = GR.Unnamed.PlayerClass;
    protected static final UnnamedImages IMAGES = GR.Unnamed.Images;

    protected Color borderIndicatorColor;

    protected static EYBCardData Register(Class<? extends UnnamedCard> type)
    {
        final EYBCardData data = RegisterCardData(type, GR.Unnamed.CreateID(type.getSimpleName()), GR.Unnamed)
        .SetColor(GR.Unnamed.CardColor).SetMetadataSource(GR.Unnamed.CardData);

        if (!Gdx.files.internal(data.ImagePath).exists())
        {
            data.ImagePath = GR.GetCardImage("unnamed:Placeholder");
            if (data.Metadata == null)
            {
                data.Metadata = new EYBCardMetadata();
                data.Metadata.cropPortrait = false;
            }
        }

        return data;
    }

    protected UnnamedCard(EYBCardData cardData)
    {
        super(cardData, cardData.ID, cardData.ImagePath, cardData.BaseCost, cardData.CardType, cardData.CardColor, cardData.CardRarity, cardData.CardTarget.ToCardTarget());

        SetMultiDamage(cardData.CardTarget == EYBCardTarget.ALL);
        SetAttackTarget(cardData.CardTarget);
        SetAttackType(cardData.AttackType);
    }

    protected UnnamedCard(EYBCardData data, String id, String imagePath, int cost, CardType type, CardColor color, CardRarity rarity, CardTarget target)
    {
        super(data, id, imagePath, cost, type, color, rarity, target);
    }

    @Override
    public AbstractPlayer.PlayerClass GetPlayerClass()
    {
        return GR.Unnamed.PlayerClass;
    }

    @Override
    protected Texture GetCardBackground()
    {
        if (color == GR.Unnamed.CardColor)
        {
            switch (type)
            {
                case ATTACK: return IMAGES.CARD_BACKGROUND_ATTACK.Texture();
                case POWER: return IMAGES.CARD_BACKGROUND_POWER.Texture();
                default: return IMAGES.CARD_BACKGROUND_SKILL.Texture();
            }
        }
        else if (color == CardColor.COLORLESS || color == CardColor.CURSE)
        {
            switch (type)
            {
                case ATTACK: return ANIMATOR_IMAGES.CARD_BACKGROUND_ATTACK_UR.Texture();
                case POWER: return ANIMATOR_IMAGES.CARD_BACKGROUND_POWER_UR.Texture();
                default: return ANIMATOR_IMAGES.CARD_BACKGROUND_SKILL_UR.Texture();
            }
        }

        return super.GetCardBackground();
    }

    @Override
    protected Texture GetEnergyOrb()
    {
        return (color == GR.Unnamed.CardColor ? IMAGES.CARD_ENERGY_ORB_UNNAMED : ANIMATOR_IMAGES.CARD_ENERGY_ORB_COLORLESS).Texture();
    }

    @Override
    protected AdvancedTexture GetCardBorderIndicator()
    {
        return borderIndicatorColor == null ? null : new AdvancedTexture(IMAGES.CARD_BORDER_INDICATOR.Texture(), borderIndicatorColor);
    }

    @Override
    protected AdvancedTexture GetCardBanner()
    {
        return new AdvancedTexture(IMAGES.CARD_BANNER_GENERIC.Texture(), GetRarityColor(false));
    }

    @Override
    protected AdvancedTexture GetPortraitFrame()
    {
        switch (type)
        {
            case ATTACK:
                return new AdvancedTexture(IMAGES.CARD_FRAME_ATTACK.Texture(), GetRarityColor(false));

            case POWER:
                return new AdvancedTexture(IMAGES.CARD_FRAME_POWER.Texture(), GetRarityColor(false));

            case SKILL:
            case CURSE:
            case STATUS:
            default:
                return new AdvancedTexture(IMAGES.CARD_FRAME_SKILL.Texture(), GetRarityColor(false));
        }
    }

    protected static boolean IsSolo()
    {
        return !CombatStats.Dolls.Any();
    }

    protected static ArrayList<AbstractCreature> GetAllies()
    {
        final ArrayList<AbstractCreature> list = new ArrayList<>();
        list.add(player);
        list.addAll(CombatStats.Dolls.GetAll());
        return list;
    }

    protected static ArrayList<UnnamedDoll> GetDolls()
    {
        return CombatStats.Dolls.GetAll();
    }

    @Override
    protected boolean CanPlayOnMinion()
    {
        return CombatStats.Dolls.Any();
    }

    @Override
    public void triggerOnGlowCheck()
    {
        super.triggerOnGlowCheck();

        if (hasTag(SUMMON) && player.hasPower(SummoningSicknessPower.POWER_ID))
        {
            this.glowColor = redGlowColor;
        }
    }

    public void SetSummon(boolean value)
    {
        SetTag(SUMMON, value);
    }

    public void SetAttachment(boolean value)
    {
        SetTag(ATTACHMENT, value);
    }

    @Override
    public ColoredString GetHeaderText()
    {
        for (CardTags t : tags)
        {
            if (t == SUMMON)
            {
                return new ColoredString(GR.Tooltips.Summon.title, Colors.Cream(1));
            }
            else if (t == ATTACHMENT)
            {
                return new ColoredString(GR.Tooltips.Attachment.title, Colors.Cream(1));
            }
        }

        return null;
    }

    @Override
    public final void use(AbstractPlayer p, AbstractMonster m)
    {
        if (attackTarget == EYBCardTarget.Minion && !GameUtilities.IsPlayerMinion(m))
        {
            GameActions.Top.SelectDoll(name)
            .AutoSelectSingleTarget(true)
            .IsCancellable(false)
            .AddCallback(doll ->
            {
                if (doll != null)
                {
                    OnUse(player, (AbstractMonster) doll);
                }
            });
        }
        else
        {
            OnUse(p, m);
        }
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        OnUse(p, m);
    }

    public abstract void OnUse(AbstractPlayer p, AbstractMonster m);
}