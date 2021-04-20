package eatyourbeets.cards.animator.beta.series.Rewrite;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.actions.cardManipulation.RandomCardUpgrade;
import eatyourbeets.cards.base.*;
import eatyourbeets.powers.common.AgilityPower;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameUtilities;

public class SougenEsaka extends AnimatorCard
{
    public static final EYBCardData DATA = Register(SougenEsaka.class).SetAttack(1, CardRarity.COMMON, EYBAttackType.Normal, EYBCardTarget.ALL);

    public SougenEsaka()
    {
        super(DATA);

        Initialize(5, 0, 1, 2);
        SetUpgrade(3, 0, 2);

        SetMartialArtist();

        SetSynergy(Synergies.Rewrite);
    }

    @Override
    protected float ModifyBlock(AbstractMonster enemy, float amount)
    {
        int agility = GameUtilities.GetPowerAmount(AbstractDungeon.player, AgilityPower.POWER_ID);
        return super.ModifyBlock(enemy, amount + (agility * secondaryValue));
    }


    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, boolean isSynergizing)
    {
        GameActions.Bottom.DealDamageToAll(this, AbstractGameAction.AttackEffect.BLUNT_LIGHT);

        int agility = GameUtilities.GetPowerAmount(p, AgilityPower.POWER_ID);
        if (agility > 0)
        {
            GameActions.Bottom.GainBlock(block);
        }

        if (HasSynergy())
        {
            GameActions.Bottom.Add(new RandomCardUpgrade());
        }
    }
}