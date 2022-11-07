package eatyourbeets.cards.animatorClassic.status;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.base.*;
import eatyourbeets.utilities.GameActions;

public class GoblinSoldier extends AnimatorClassicCard
{
    public static final EYBCardData DATA = Register(GoblinSoldier.class).SetStatus(1, CardRarity.COMMON, EYBCardTarget.None);

    public GoblinSoldier()
    {
        super(DATA);

        Initialize(0, 0, 2);

        SetEndOfTurnPlay(true);
        this.series = CardSeries.GoblinSlayer;
    }

    @Override
    public void triggerWhenDrawn()
    {
        super.triggerWhenDrawn();

        GameActions.Bottom.Draw(1);
        GameActions.Bottom.Flash(this);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        if (this.dontTriggerOnUseCard)
        {
            GameActions.Bottom.DealDamage(p, p, magicNumber, DamageInfo.DamageType.THORNS, AbstractGameAction.AttackEffect.SLASH_HORIZONTAL);
        }
    }
}