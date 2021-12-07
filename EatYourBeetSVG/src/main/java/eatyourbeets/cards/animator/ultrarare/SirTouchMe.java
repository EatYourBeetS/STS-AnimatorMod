package eatyourbeets.cards.animator.ultrarare;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.JuggernautPower;
import eatyourbeets.cards.base.*;
import eatyourbeets.effects.AttackEffects;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameEffects;

public class SirTouchMe extends AnimatorCard_UltraRare
{
    public static final EYBCardData DATA = Register(SirTouchMe.class)
            .SetAttack(2, CardRarity.SPECIAL, EYBAttackType.Brutal)
            .SetColor(CardColor.COLORLESS)
            .SetSeries(CardSeries.Overlord);

    public SirTouchMe()
    {
        super(DATA);

        Initialize(4, 4, 4, 3);
        SetUpgrade(2, 2, 2, 0);
        
        SetAffinity_Red(2, 0, 2);
        SetAffinity_Light(2, 0, 1);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        GameActions.Bottom.GainBlock(block);
        GameActions.Bottom.DealCardDamage(this, m, AttackEffects.SLASH_HEAVY);
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