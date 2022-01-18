package pinacolada.cards.pcl.curse;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.base.EYBCardTarget;
import eatyourbeets.powers.CombatStats;
import pinacolada.cards.base.CardUseInfo;
import pinacolada.cards.base.PCLCardData;
import pinacolada.cards.base.PCLCard_Curse;
import pinacolada.utilities.PCLActions;

public class Curse_Pain extends PCLCard_Curse
{
    public static final PCLCardData DATA = Register(Curse_Pain.class)
            .SetCurse(-2, EYBCardTarget.None, false);

    public Curse_Pain()
    {
        super(DATA, true);

        Initialize(0, 0, 1);

        SetUnplayable(true);
    }

    @Override
    public void triggerOnExhaust()
    {
        super.triggerOnExhaust();
        if (CombatStats.TryActivateLimited(cardID)) {
            for (int i = 0; i < AbstractDungeon.actionManager.cardsPlayedThisTurn.size(); i++) {
                PCLActions.Bottom.DealDamageToRandomEnemy(1, DamageInfo.DamageType.THORNS, AbstractGameAction.AttackEffect.NONE);
            }
        }
    }

    @Override
    public void triggerOnOtherCardPlayed(AbstractCard c)
    {
        super.triggerOnOtherCardPlayed(c);

        if (player.hand.contains(this))
        {
            PCLActions.Bottom.LoseHP(magicNumber, AbstractGameAction.AttackEffect.NONE);
        }
    }

    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
    }
}