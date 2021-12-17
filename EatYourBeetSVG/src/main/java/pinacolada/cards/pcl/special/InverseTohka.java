package pinacolada.cards.pcl.special;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.powers.CombatStats;
import pinacolada.cards.base.*;
import pinacolada.effects.AttackEffects;
import pinacolada.utilities.PCLActions;

public class InverseTohka extends PCLCard
{
    public static final PCLCardData DATA = Register(InverseTohka.class).SetAttack(2, CardRarity.SPECIAL, PCLAttackType.Normal, eatyourbeets.cards.base.EYBCardTarget.ALL).SetSeries(CardSeries.DateALive);

    public InverseTohka()
    {
        super(DATA);

        Initialize(8, 0, 2, 1);
        SetUpgrade(3, 0);
        SetAffinity_Red(1, 0, 2);
        SetAffinity_Dark(1, 0, 0);

        SetAutoplay(true);
    }

    @Override
    protected float ModifyDamage(AbstractMonster enemy, float amount)
    {
        return super.ModifyDamage(enemy, amount + CombatStats.SynergiesThisCombat().size() * magicNumber);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        PCLActions.Bottom.DealCardDamageToAll(this, AttackEffects.SLASH_HEAVY);
        PCLActions.Bottom.SelectFromPile(name, magicNumber, player.drawPile, player.hand, player.discardPile)
                .SetOptions(true, true)
                .SetFilter(c -> c instanceof PCLCard && this.series.equals(((PCLCard) c).series))
                .AddCallback(cards ->
                {
                    for (AbstractCard c : cards) {
                        PCLActions.Bottom.Motivate(c, 1);
                    }
                });
    }
}