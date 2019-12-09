package eatyourbeets.cards.animator;

import basemod.BaseMod;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.actions.unique.BouncingFlaskAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.orbs.AbstractOrb;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.vfx.combat.PotionBounceEffect;
import eatyourbeets.cards.EYBCardBadge;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameActionsHelper_Legacy;
import eatyourbeets.actions._legacy.common.VariableDiscardAction;
import eatyourbeets.cards.AnimatorCard;
import eatyourbeets.cards.Synergies;
import eatyourbeets.utilities.GameUtilities;

import java.util.ArrayList;

@SuppressWarnings("SuspiciousNameCombination")
public class Layla extends AnimatorCard
{
    public static final String ID = Register(Layla.class.getSimpleName(), EYBCardBadge.Special);

    public Layla()
    {
        super(ID, 2, CardType.ATTACK, CardRarity.UNCOMMON, CardTarget.ENEMY);

        Initialize(7, 0, 2, 2);

        SetSynergy(Synergies.Chaika);
    }

    @Override
    public float calculateModifiedCardDamage(AbstractPlayer player, AbstractMonster mo, float tmp)
    {
        int debuffs = 0;
        if (mo != null) for (AbstractPower power : mo.powers)
        {
            if (power.type == AbstractPower.PowerType.DEBUFF)
            {
                debuffs += 1;
            }
        }

        return super.calculateModifiedCardDamage(player, mo, tmp + (debuffs * secondaryValue));
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m)
    {
        GameActions.Bottom.DealDamage(this, m, AbstractGameAction.AttackEffect.BLUNT_LIGHT).SetOptions(true, true);
        GameActions.Bottom.Reload(name, cards ->
        {
            if (cards.size() > 0)
            {
                AbstractPlayer player = AbstractDungeon.player;
                AbstractMonster enemy = GameUtilities.GetRandomEnemy(true);

                if (enemy != null)
                {
                    GameActions.Bottom.VFX(new PotionBounceEffect(player.hb.cY, player.hb.cX, enemy.hb.cX, enemy.hb.cY), 0.3F);
                }

                GameActions.Bottom.Add(new BouncingFlaskAction(enemy, this.magicNumber, cards.size()));
            }
        });
    }

    @Override
    public void upgrade()
    {
        if (TryUpgrade())
        {
            upgradeMagicNumber(1);
        }
    }
}