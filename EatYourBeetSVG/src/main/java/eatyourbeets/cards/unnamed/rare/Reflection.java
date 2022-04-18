package eatyourbeets.cards.unnamed.rare;

import com.evacipated.cardcrawl.mod.stslib.powers.StunMonsterPower;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.cards.base.EYBCardTarget;
import eatyourbeets.cards.base.UnnamedCard;
import eatyourbeets.monsters.PlayerMinions.UnnamedDoll;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameUtilities;

import java.util.ArrayList;

public class Reflection extends UnnamedCard
{
    public static final EYBCardData DATA = Register(Reflection.class)
            .SetSkill(2, CardRarity.RARE, EYBCardTarget.Minion);

    public Reflection()
    {
        super(DATA);

        Initialize(0, 0);

        SetInnate(true);
        SetExhaust(true);
        SetEthereal(true);
    }

    @Override
    protected void OnUpgrade()
    {
        SetEthereal(false);
    }

    @Override
    protected void Refresh(AbstractMonster enemy)
    {
        super.Refresh(enemy);

        SetAttackTarget(IsSolo() ? EYBCardTarget.None : EYBCardTarget.Minion);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m)
    {
        if (IsSolo() || m == null)
        {
            final ArrayList<String> attacks = new ArrayList<>();
            for (AbstractCard c : GameUtilities.GetOtherCardsInHand(this))
            {
                if (c.type == CardType.ATTACK)
                {
                    attacks.add(c.cardID);
                }
            }

            GameActions.Bottom.Draw(999)
            .SetFilter(attacks, (cards, b) -> cards.remove(b.cardID), false)
            .AddCallback(cards ->
            {
                for (AbstractCard c : cards)
                {
                    GameActions.Bottom.Motivate(c, 1);
                }
            });
        }
        else
        {
            GameActions.Bottom.ApplyPower(new StunMonsterPower(m));
            GameActions.Bottom.SummonDoll(1).CloneFrom((UnnamedDoll) m);
        }
    }
}