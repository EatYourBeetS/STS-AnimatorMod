package eatyourbeets.cards.unnamed.common;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.base.EYBAttackType;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.cards.base.EYBCardTarget;
import eatyourbeets.cards.base.UnnamedCard;
import eatyourbeets.powers.unnamed.CleavingAttacksPower;
import eatyourbeets.utilities.GameActions;

public class CleavingAttack extends UnnamedCard
{
    public static final EYBCardData DATA = Register(CleavingAttack.class)
            .SetAttack(1, CardRarity.COMMON, EYBAttackType.Normal, EYBCardTarget.ALL);

    public CleavingAttack()
    {
        super(DATA);

        Initialize(3, 0, 1, 1);
        SetUpgrade(3, 0);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m)
    {
        for (int i = 0; i < magicNumber; i++)
        {
            GameActions.Bottom.DealDamageToAll(this, AbstractGameAction.AttackEffect.SLASH_HORIZONTAL);
        }

        GameActions.Bottom.SelectDoll(name)
        .AutoSelectSingleTarget(true)
        .AddCallback(doll ->
        {
            if (doll != null)
            {
                GameActions.Bottom.GainStrength(player, doll, secondaryValue);
                GameActions.Bottom.ApplyPower(new CleavingAttacksPower(doll));
            }
        });
    }
}