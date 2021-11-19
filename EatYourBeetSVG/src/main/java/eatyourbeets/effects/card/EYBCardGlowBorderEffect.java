package eatyourbeets.effects.card;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
import com.badlogic.gdx.math.Interpolation;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import eatyourbeets.effects.EYBEffect;
import eatyourbeets.utilities.GameUtilities;
import patches.CardGlowBorderPatches;

public class EYBCardGlowBorderEffect extends EYBEffect {

    public static final Color FALLBACK_COLOR = Color.valueOf("30c8dcff");
    protected AbstractCard card;
    protected AtlasRegion img;
    protected float scale;

    public EYBCardGlowBorderEffect(AbstractCard card) {
        this(card, FALLBACK_COLOR);
    }

    public EYBCardGlowBorderEffect(AbstractCard card, Color gColor) {
        this.card = card;
        switch(card.type) {
            case POWER:
                this.img = ImageMaster.CARD_POWER_BG_SILHOUETTE;
                break;
            case ATTACK:
                this.img = ImageMaster.CARD_ATTACK_BG_SILHOUETTE;
                break;
            default:
                this.img = ImageMaster.CARD_SKILL_BG_SILHOUETTE;
        }

        this.duration = 1.2F;
        if (GameUtilities.InBattle(false)) {
            this.color = gColor.cpy();
        }
        else if (CardGlowBorderPatches.overrideColor != null) {
            this.color = CardGlowBorderPatches.overrideColor.cpy();
        }
        else {
            this.color = FALLBACK_COLOR;
        }

    }

    public void update() {
        this.scale = (1.0F + Interpolation.pow2Out.apply(0.03F, 0.11F, 1.0F - this.duration)) * this.card.drawScale * Settings.scale;
        this.color.a = this.duration / 2.0F;
        this.duration -= Gdx.graphics.getDeltaTime();
        if (this.duration < 0.0F) {
            this.isDone = true;
            this.duration = 0.0F;
        }

    }

    public void render(SpriteBatch sb) {
        sb.setColor(this.color);
        sb.draw(this.img, this.card.current_x + this.img.offsetX - (float)this.img.originalWidth / 2.0F, this.card.current_y + this.img.offsetY - (float)this.img.originalHeight / 2.0F, (float)this.img.originalWidth / 2.0F - this.img.offsetX, (float)this.img.originalHeight / 2.0F - this.img.offsetY, (float)this.img.packedWidth, (float)this.img.packedHeight, this.scale, this.scale, this.card.angle);
    }

    public void dispose() {
    }
}