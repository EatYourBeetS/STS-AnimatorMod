package eatyourbeets.cards.animator;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.actions.unique.LoseEnergyAction;
import com.megacrit.cardcrawl.actions.utility.ShakeScreenAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.helpers.ScreenShake;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.ui.panels.EnergyPanel;
import com.megacrit.cardcrawl.vfx.combat.VerticalImpactEffect;
import eatyourbeets.GameActionsHelper;
import eatyourbeets.cards.AnimatorCard;
import eatyourbeets.cards.Synergies;

public class Saitama extends AnimatorCard
{
    public static final String ID = CreateFullID(Saitama.class.getSimpleName());

    private static final int DAMAGE_STEP = 10;

    public Saitama()
    {
        super(ID, 2, CardType.ATTACK, CardColor.COLORLESS, CardRarity.RARE, CardTarget.ENEMY);

        Initialize(20, 0);

        AddExtendedDescription();

        SetSynergy(Synergies.OnePunchMan);
    }

    @Override
    public float calculateModifiedCardDamage(AbstractPlayer player, AbstractMonster mo, float tmp)
    {
        if (!player.drawPile.contains(this) && !player.discardPile.contains(this) && !player.exhaustPile.contains(this))
        {
            int energy = EnergyPanel.getCurrentEnergy() - 2;
            int multiplier = Math.floorDiv((energy * (energy + 1)), 2);

            if (energy > 3)
            {
                multiplier += Math.floorDiv(energy, 4);
            }

            float bonusDamage = DAMAGE_STEP * multiplier;

            if (upgraded)
            {
                bonusDamage += (Math.floorDiv(energy, 3) + 1) * 10;
            }

            tmp += bonusDamage;
            if (tmp > 99999)
            {
                tmp = 99999;
            }
        }

        return super.calculateModifiedCardDamage(player, mo, tmp);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m)
    {
        GameActionsHelper.AddToBottom(new VFXAction(new VerticalImpactEffect(m.hb.cX + m.hb.width / 4.0F, m.hb.cY - m.hb.height / 4.0F)));

        GameActionsHelper.DamageTarget(p, m, this, AbstractGameAction.AttackEffect.NONE);

        GameActionsHelper.AddToBottom(new ShakeScreenAction(0.5f, ScreenShake.ShakeDur.MED, ScreenShake.ShakeIntensity.MED));
        GameActionsHelper.AddToBottom(new LoseEnergyAction(EnergyPanel.getCurrentEnergy()));
    }

    @Override
    public void upgrade()
    {
        TryUpgrade();
    }
}