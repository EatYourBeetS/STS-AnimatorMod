package eatyourbeets.cards.animatorClassic.ultrarare;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.JuggernautPower;
import eatyourbeets.cards.base.*;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameEffects;

public class SirTouchMe extends AnimatorClassicCard_UltraRare
{
    public static final EYBCardData DATA = Register(SirTouchMe.class).SetAttack(2, CardRarity.SPECIAL).SetColor(CardColor.COLORLESS);

    public SirTouchMe()
    {
        super(DATA);

        Initialize(4, 4, 4, 3);
        SetUpgrade(2, 2, 2, 0);
        SetScaling(0, 0, 2);

        this.series = CardSeries.Overlord;
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        GameActions.Bottom.GainBlock(block);
        GameActions.Bottom.DealDamage(this, m, AbstractGameAction.AttackEffect.SLASH_HEAVY);
        GameActions.Bottom.StackPower(new JuggernautPower(p, magicNumber));
    }

    @Override
    public void triggerWhenCreated(boolean startOfBattle)
    {
        super.triggerWhenCreated(startOfBattle);

        if (startOfBattle)
        {
            GameEffects.List.ShowCopy(this);
            GameActions.Bottom.GainPlatedArmor(secondaryValue);
        }
    }
}