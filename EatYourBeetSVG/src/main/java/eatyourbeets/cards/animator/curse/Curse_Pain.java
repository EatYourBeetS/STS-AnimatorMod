package eatyourbeets.cards.animator.curse;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.base.AnimatorCard_Curse;
import eatyourbeets.cards.base.CardUseInfo;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.cards.base.EYBCardTarget;
import eatyourbeets.powers.CombatStats;
import eatyourbeets.utilities.GameActions;

public class Curse_Pain extends AnimatorCard_Curse
{
    public static final EYBCardData DATA = Register(Curse_Pain.class)
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
                GameActions.Bottom.DealDamageToRandomEnemy(1, DamageInfo.DamageType.THORNS, AbstractGameAction.AttackEffect.NONE);
            }
        }
    }

    @Override
    public void triggerOnOtherCardPlayed(AbstractCard c)
    {
        super.triggerOnOtherCardPlayed(c);

        if (player.hand.contains(this))
        {
            GameActions.Bottom.LoseHP(magicNumber, AbstractGameAction.AttackEffect.NONE);
        }
    }

    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
    }
}