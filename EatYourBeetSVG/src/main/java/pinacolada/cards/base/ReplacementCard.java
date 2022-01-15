package pinacolada.cards.base;

import basemod.BaseMod;
import basemod.abstracts.CustomCard;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Vector2;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.base.EYBCard;
import eatyourbeets.utilities.AdvancedTexture;
import eatyourbeets.utilities.ColoredString;
import eatyourbeets.utilities.Colors;
import eatyourbeets.utilities.FieldInfo;
import pinacolada.utilities.PCLJUtils;
import pinacolada.utilities.PCLRenderHelpers;

public class ReplacementCard extends PCLCard_Dynamic
{
    protected final static FieldInfo<Color> _bgColor = PCLJUtils.GetField("bgColor", AbstractCard.class);
    protected final static Color CARD_COLOR_BLUE = new Color(0.37f, 0.63f, 0.82f, 1f);
    protected final static Color CARD_COLOR_COLORLESS = new Color(0.6f, 0.6f, 0.6f, 1f);
    protected final static Color CARD_COLOR_CURSE = new Color(0.22f, 0.22f, 0.22f, 1f);
    protected final static Color CARD_COLOR_GREEN = new Color(0.56f, 0.77f, 0.4f, 1f);
    protected final static Color CARD_COLOR_PURPLE = new Color(0.55f, 0.4f, 0.8f, 1f);
    protected final static Color CARD_COLOR_RED = new Color(0.88f, 0.4f, 0.4f, 1f);

    protected final ReplacementCardBuilder builder;
    protected AbstractCard original;
    protected EYBCard eybCard;
    protected Color cardColor;
    protected TextureAtlas.AtlasRegion originalEnergyOrb;

    public ReplacementCard(ReplacementCardBuilder builder)
    {
        super(builder);
        this.builder = builder;
        this.original = builder.original;
        this.eybCard = PCLJUtils.SafeCast(this.original, EYBCard.class);
        this.cardColor = GetOriginalColor(this.original);

        this.originalEnergyOrb = GetOriginalEnergyOrb(this.original);
    }

    @Override
    protected Texture GetCardBackground()
    {
        switch (type)
        {
            case ATTACK: return isPopup ? PCLCard.IMAGES.CARD_BACKGROUND_ATTACK_REPL_L.Texture() : PCLCard.IMAGES.CARD_BACKGROUND_ATTACK_REPL.Texture();
            case POWER: return isPopup ? PCLCard.IMAGES.CARD_BACKGROUND_POWER_REPL_L.Texture() : PCLCard.IMAGES.CARD_BACKGROUND_POWER_REPL.Texture();
            default: return isPopup ? PCLCard.IMAGES.CARD_BACKGROUND_SKILL_REPL_L.Texture() : PCLCard.IMAGES.CARD_BACKGROUND_SKILL_REPL.Texture();
        }
    }

    @Override
    protected AdvancedTexture GetCardBanner()
    {
        return new AdvancedTexture((isPopup ? PCLCard.IMAGES.CARD_BANNER_REPL_L : PCLCard.IMAGES.CARD_BANNER_REPL).Texture(), GetRarityColor(false));
    }

    @Override
    protected Texture GetEnergyOrb()
    {

        CustomCard cCard = PCLJUtils.SafeCast(original, CustomCard.class);
        if (cCard != null) {
            Texture t = isPopup ? cCard.getOrbLargeTexture() : cCard.getOrbSmallTexture();
            if (t == null) {
                t = ImageMaster.loadImage(isPopup ? BaseMod.getEnergyOrbPortrait(original.color) : BaseMod.getEnergyOrb(original.color));
                BaseMod.saveEnergyOrbPortraitTexture(original.color, t);
            }
            return t;
        }
        return null;
    }

    @Override
    protected AdvancedTexture GetPortraitFrame()
    {
        switch (type)
        {
            case ATTACK:
                return new AdvancedTexture(isPopup ? PCLCard.IMAGES.CARD_FRAME_ATTACK_REPL_L.Texture() : PCLCard.IMAGES.CARD_FRAME_ATTACK_REPL.Texture(), GetRarityColor(false));

            case POWER:
                return new AdvancedTexture(isPopup ? PCLCard.IMAGES.CARD_FRAME_POWER_REPL_L.Texture() : PCLCard.IMAGES.CARD_FRAME_POWER_REPL.Texture(), GetRarityColor(false));

            case SKILL:
            case CURSE:
            case STATUS:
            default:
                return new AdvancedTexture(isPopup ? PCLCard.IMAGES.CARD_FRAME_SKILL_REPL_L.Texture() : PCLCard.IMAGES.CARD_FRAME_SKILL_REPL.Texture(), GetRarityColor(false));
        }
    }

    @Override
    protected void renderCardBg(SpriteBatch sb, float x, float y)
    {
        Texture card = GetCardBackground();
        float popUpMultiplier = isPopup ? 0.5f : 1f;
        Color renderColor = cardColor.cpy();
        renderColor.a = transparency;

        if (this.original.color == CardColor.COLORLESS || this.original.color == CardColor.CURSE) {
            pinacolada.utilities.PCLRenderHelpers.DrawGrayscale(sb, () ->
                pinacolada.utilities.PCLRenderHelpers.DrawOnCardAuto(sb, this, card, new Vector2(0,0), card.getWidth(), card.getHeight(), renderColor, transparency, popUpMultiplier));
        }
        else {
            pinacolada.utilities.PCLRenderHelpers.DrawOnCardAuto(sb, this, card, new Vector2(0,0), card.getWidth(), card.getHeight(), renderColor, transparency, popUpMultiplier);
        }
    }

    @Override
    protected void renderEnergy(SpriteBatch sb)
    {
        if (this.cost > -2 && !_darken.Get(this) && !this.isLocked && this.isSeen)
        {
            Texture energyOrb = GetEnergyOrb();
            if (energyOrb != null) {
                Texture baseCard = GetCardBackground();
                float popUpMultiplier = isPopup ? 0.5f : 1f;
                pinacolada.utilities.PCLRenderHelpers.DrawOnCardAuto(sb, this, energyOrb, new Vector2(0,0), energyOrb.getWidth(), energyOrb.getHeight(), _renderColor.Get(this), transparency, popUpMultiplier);
                ColoredString costString = GetCostString();
                if (costString != null)
                {
                    BitmapFont font = pinacolada.utilities.PCLRenderHelpers.GetEnergyFont(this);
                    pinacolada.utilities.PCLRenderHelpers.WriteOnCard(sb, this, font, costString.text, -132f, 192f, costString.color);
                    pinacolada.utilities.PCLRenderHelpers.ResetFont(font);
                }
            }
            else if (this.originalEnergyOrb != null) {
                this.RenderAtlas(sb, _renderColor.Get(this), this.originalEnergyOrb, this.current_x, this.current_y);

                ColoredString costString = GetCostString();
                if (costString != null)
                {
                    BitmapFont font = PCLRenderHelpers.GetEnergyFont(this);
                    pinacolada.utilities.PCLRenderHelpers.WriteOnCard(sb, this, font, costString.text, -132f, 192f, costString.color);
                    PCLRenderHelpers.ResetFont(font);
                }
            }
        }
    }

    @Override
    public void triggerOnEndOfTurnForPlayingCard() {
        super.triggerOnEndOfTurnForPlayingCard();
        original.triggerOnEndOfTurnForPlayingCard();
    }

    @Override
    public void triggerOnExhaust() {
        super.triggerOnExhaust();
        original.triggerOnExhaust();
    }

    @Override
    public void triggerOnManualDiscard() {
        super.triggerOnManualDiscard();
        original.triggerOnExhaust();
    }

    @Override
    public void triggerWhenCreated(boolean startOfBattle) {
        super.triggerWhenCreated(startOfBattle);

        if (eybCard != null) {
            eybCard.triggerWhenCreated(startOfBattle);
        }
    }

    @Override
    public void triggerWhenDrawn() {
        super.triggerWhenDrawn();
        original.triggerWhenDrawn();
    }

    @Override
    public boolean cardPlayable(AbstractMonster m) {
        return original.cardPlayable(m);
    }

    @Override
    public AbstractCard makeCopy() {
        return new ReplacementCard(builder);
    }

    @Override
    protected void OnUpgrade() {
        super.OnUpgrade();
        original.upgrade();
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        if (original != null)
        {
            UpdateOriginal();
            original.use(player, m);
        }
    }

    protected Color GetOriginalColor(AbstractCard original) {
        if (original.color == CardColor.RED) {
            return CARD_COLOR_RED;
        }
        else if (original.color == CardColor.GREEN) {
            return CARD_COLOR_GREEN;
        }
        else if (original.color == CardColor.BLUE) {
            return CARD_COLOR_BLUE;
        }
        else if (original.color == CardColor.PURPLE) {
            return CARD_COLOR_PURPLE;
        }
        else if (original.color == CardColor.CURSE) {
            return CARD_COLOR_CURSE;
        }
        else if (original.color == CardColor.COLORLESS) {
            return CARD_COLOR_COLORLESS;
        }
        Color newColor = _bgColor.Get(this.original);
        if (newColor == null) {
            newColor = Color.WHITE.cpy();
        }
        else {
            newColor = Colors.Lerp(Color.WHITE, newColor, 0.85f);
        }
        return newColor;
    }

    protected TextureAtlas.AtlasRegion GetOriginalEnergyOrb(AbstractCard original) {
        switch (original.color) {
            case RED:
                return ImageMaster.CARD_RED_ORB;
            case GREEN:
                return ImageMaster.CARD_GREEN_ORB;
            case BLUE:
                return ImageMaster.CARD_BLUE_ORB;
            case PURPLE:
                return ImageMaster.CARD_PURPLE_ORB;
            case CURSE:
            case COLORLESS:
                return ImageMaster.CARD_COLORLESS_ORB;
        }
        return ImageMaster.CARD_COLORLESS_ORB;
    }

    protected void UpdateOriginal() {
        original.baseDamage = this.baseDamage;
        original.baseBlock = this.baseBlock;
        original.baseMagicNumber = this.baseMagicNumber;
        original.isDamageModified = this.isDamageModified;
        original.isBlockModified = this.isBlockModified;
        original.isMagicNumberModified = this.isMagicNumberModified;
        original.multiDamage = this.multiDamage;
        original.damage = this.damage;
        original.block = this.block;
        original.magicNumber = this.magicNumber;
        original.costForTurn = this.costForTurn;
    }
}