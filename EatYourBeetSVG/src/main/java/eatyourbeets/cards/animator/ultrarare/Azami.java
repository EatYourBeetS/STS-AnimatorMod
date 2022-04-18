package eatyourbeets.cards.animator.ultrarare;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.animator.status.Status_Dazed;
import eatyourbeets.cards.base.*;
import eatyourbeets.interfaces.subscribers.OnEndOfTurnLastSubscriber;
import eatyourbeets.monsters.EnemyIntent;
import eatyourbeets.powers.CombatStats;
import eatyourbeets.powers.PowerHelper;
import eatyourbeets.utilities.*;

public class Azami extends AnimatorCard_UltraRare implements OnEndOfTurnLastSubscriber
{
    public static final EYBCardData DATA = Register(Azami.class)
            .SetSkill(1, CardRarity.SPECIAL, EYBCardTarget.ALL)
            .SetMaxCopies(1)
            .ObtainableAsReward((data, deck) -> AnimatorCard_UltraRare.IsSeen(data.ID) && JUtils.Count(deck.group, GameUtilities::HasDarkAffinity) > deck.size() / 4)
            .SetColor(CardColor.COLORLESS);
    public static final int SHACKLES_AMOUNT = 9;

    public Azami()
    {
        super(DATA);

        Initialize(0, 0, 12, 16);
        SetCostUpgrade(-1);

        SetAffinity_Dark(1);

        SetEthereal(true);
        SetExhaust(true);
        SetDelayed(true);
    }

    @Override
    public ColoredString GetSpecialVariableString()
    {
        return new ColoredString(SHACKLES_AMOUNT, Colors.Cream(1));
    }

    @Override
    public void OnDrag(AbstractMonster m)
    {
        super.OnDrag(m);

        for (EnemyIntent intent : GameUtilities.GetIntents())
        {
            intent.AddStrength(-SHACKLES_AMOUNT);
        }
    }

    @Override
    public void OnEndOfTurnLast(boolean isPlayer)
    {
        if (player.exhaustPile.contains(this))
        {
            GameActions.Bottom.GainCorruption(1, true);
            GameActions.Bottom.MakeCardInDrawPile(new Status_Dazed()).SetDestination(CardSelection.Top);
        }
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        GameActions.Bottom.StackPower(TargetHelper.Enemies(p), PowerHelper.Shackles, SHACKLES_AMOUNT);
        GameActions.Bottom.StackPower(TargetHelper.Enemies(p), PowerHelper.Poison, magicNumber);
        GameActions.Bottom.StackPower(TargetHelper.Enemies(p), PowerHelper.Constricted, secondaryValue);
    }

    @Override
    public void triggerWhenCreated(boolean startOfBattle)
    {
        super.triggerWhenCreated(startOfBattle);

        CombatStats.onEndOfTurnLast.Subscribe(this);
    }
}