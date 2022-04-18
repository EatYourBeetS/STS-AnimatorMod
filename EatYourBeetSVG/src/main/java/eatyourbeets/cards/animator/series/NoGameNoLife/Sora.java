package eatyourbeets.cards.animator.series.NoGameNoLife;

import com.megacrit.cardcrawl.actions.utility.UseCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.animator.special.Sora_BattlePlan1;
import eatyourbeets.cards.animator.special.Sora_BattlePlan2;
import eatyourbeets.cards.animator.special.Sora_BattlePlan3;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.CardUseInfo;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.cards.base.EYBCardTarget;
import eatyourbeets.powers.AnimatorPower;
import eatyourbeets.utilities.CardSelection;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameUtilities;
import eatyourbeets.utilities.JUtils;

public class Sora extends AnimatorCard
{
    public static final EYBCardData DATA = Register(Sora.class)
            .SetSkill(2, CardRarity.RARE, EYBCardTarget.None)
            .SetSeriesFromClassPackage()
            .PostInitialize(data ->
            {
                data.AddPreview(new Sora_BattlePlan1(), false);
                data.AddPreview(new Sora_BattlePlan2(), false);
                data.AddPreview(new Sora_BattlePlan3(), false);
            });

    public Sora()
    {
        super(DATA);

        Initialize(0, 0);

        SetAffinity_Blue(2);
        SetAffinity_Light(2);

        SetExhaust(true);
    }

    @Override
    protected void OnUpgrade()
    {
        SetExhaust(false);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        GameActions.Bottom.StackPower(new SoraPower(p, 1));

        if (info.TryActivateLimited())
        {
            final CardGroup group = new CardGroup(CardGroup.CardGroupType.UNSPECIFIED);
            group.group.add(new Sora_BattlePlan1());
            group.group.add(new Sora_BattlePlan2());
            group.group.add(new Sora_BattlePlan3());
            GameActions.Bottom.SelectFromPile(name, 1, group)
            .SetOptions(false, false)
            .AddCallback(cards ->
            {
                for (AbstractCard card : cards)
                {
                    GameActions.Bottom.MakeCardInDrawPile(card)
                    .SetDestination(CardSelection.Special((list, c, index) ->
                    {
                        index += rng.random(list.size() / 2);
                        index = Math.max(0, Math.min(list.size(), list.size() - index));
                        list.add(index, c);
                    }, null));
                }
            });
        }
    }

    public static class SoraPower extends AnimatorPower
    {
        public SoraPower(AbstractCreature owner, int amount)
        {
            super(owner, Sora.DATA);

            Initialize(amount);
        }

        @Override
        public void onUseCard(AbstractCard card, UseCardAction action)
        {
            super.onUseCard(card, action);

            if (GameUtilities.IsLowCost(card) && card.type == CardType.SKILL && GameUtilities.CanPlayTwice(card))
            {
                GameActions.Top.PlayCopy(card, JUtils.SafeCast(action.target, AbstractMonster.class));
                ReducePower(GameActions.Top, 1);
            }
        }
    }
}