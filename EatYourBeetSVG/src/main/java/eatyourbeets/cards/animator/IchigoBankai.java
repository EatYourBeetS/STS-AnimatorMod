package eatyourbeets.cards.animator;

import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.relics.ChemicalX;
import com.megacrit.cardcrawl.ui.panels.EnergyPanel;
import com.megacrit.cardcrawl.vfx.BorderLongFlashEffect;
import com.megacrit.cardcrawl.vfx.combat.ShockWaveEffect;
import eatyourbeets.cards.AnimatorCard;
import eatyourbeets.cards.Synergies;
import eatyourbeets.interfaces.metadata.Hidden;
import eatyourbeets.interfaces.metadata.MartialArtist;
import eatyourbeets.utilities.GameActionsHelper; import eatyourbeets.utilities.GameActionsHelper2;

public class IchigoBankai extends AnimatorCard implements MartialArtist, Hidden
{
    public static final String ID = Register(IchigoBankai.class.getSimpleName());

    public IchigoBankai()
    {
        super(ID, -1, CardType.ATTACK, CardColor.COLORLESS, CardRarity.SPECIAL, CardTarget.ALL_ENEMY);

        Initialize(8, 0);

        SetExhaust(true);
        SetMultiDamage(true);
        SetSynergy(Synergies.Bleach);
    }

    @Override
    public float calculateModifiedCardDamage(AbstractPlayer player, AbstractMonster mo, float tmp)
    {
        int effect = EnergyPanel.totalCount;
        if (this.energyOnUse != -1)
        {
            effect = this.energyOnUse;
        }

        if (AbstractDungeon.player.hasRelic(ChemicalX.ID))
        {
            effect += ChemicalX.BOOST;
        }

        return super.calculateModifiedCardDamage(player, mo, (effect) * (tmp + MartialArtist.GetScaling()));
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m)
    {
        if (!this.freeToPlayOnce)
        {
            p.energy.use(EnergyPanel.totalCount);
        }

        if (damage > 0)
        {
            GameActionsHelper.AddToTop(new VFXAction(new BorderLongFlashEffect(Color.LIGHT_GRAY)));
            GameActionsHelper.VFX(new ShockWaveEffect(p.hb.cX, p.hb.cY, Color.LIGHT_GRAY, ShockWaveEffect.ShockWaveType.ADDITIVE), 0.75F);
            GameActionsHelper.DamageAllEnemies(p, multiDamage, damageTypeForTurn, AbstractGameAction.AttackEffect.SLASH_HEAVY, false);
        }
    }

    @Override
    public void upgrade()
    {
        if (TryUpgrade())
        {
            upgradeDamage(3);
        }
    }
}