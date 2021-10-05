package eatyourbeets.cards.animator.beta.special;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.base.*;
import eatyourbeets.effects.AttackEffects;
import eatyourbeets.powers.CombatStats;
import eatyourbeets.utilities.GameActions;

public class InverseTohka extends AnimatorCard
{
    public static final EYBCardData DATA = Register(InverseTohka.class).SetAttack(1, CardRarity.SPECIAL, EYBAttackType.Normal, EYBCardTarget.ALL).SetSeries(CardSeries.DateALive);

    public InverseTohka()
    {
        super(DATA);

        Initialize(8, 0, 2, 1);
        SetUpgrade(3, 0);
        SetAffinity_Red(2, 0, 0);
        SetAffinity_Dark(1, 0, 0);

        SetAutoplay(true);
    }

    @Override
    protected float ModifyDamage(AbstractMonster enemy, float amount)
    {
        return super.ModifyDamage(enemy, amount + CombatStats.SynergiesThisCombat().size() * magicNumber);
    }

    @Override
    public void triggerWhenDrawn()
    {
        super.triggerWhenDrawn();

        GameActions.Bottom.SpendEnergy(1,false).AddCallback(() -> {
            GameActions.Bottom.SelectFromPile(name, magicNumber, player.drawPile, player.hand, player.discardPile)
                    .SetOptions(true, true)
                    .SetFilter(c -> c instanceof AnimatorCard && ((AnimatorCard) c).series.equals(this.series))
                    .AddCallback(cards ->
                    {
                        for (AbstractCard c : cards) {
                            GameActions.Bottom.Motivate(c, 1);
                        }
                    });
        });
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        GameActions.Bottom.DealDamageToAll(this, AttackEffects.SLASH_HEAVY);
    }
}