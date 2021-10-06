package eatyourbeets.cards.animator.series.OwariNoSeraph;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.cards.status.Dazed;
import com.megacrit.cardcrawl.cards.status.VoidCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.actions.special.RefreshHandLayout;
import eatyourbeets.cards.animator.beta.status.Frostbite;
import eatyourbeets.cards.animator.beta.status.SearingBurn;
import eatyourbeets.cards.animator.status.Crystallize;
import eatyourbeets.cards.animator.status.Status_Burn;
import eatyourbeets.cards.animator.status.Status_Slimed;
import eatyourbeets.cards.animator.status.Status_Wound;
import eatyourbeets.cards.base.*;
import eatyourbeets.misc.GenericEffects.GenericEffect_StackPower;
import eatyourbeets.powers.CombatStats;
import eatyourbeets.powers.PowerHelper;
import eatyourbeets.resources.GR;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameUtilities;
import eatyourbeets.utilities.RandomizedList;

public class LestKarr extends AnimatorCard
{
    public static final EYBCardData DATA = Register(LestKarr.class)
            .SetSkill(0, CardRarity.UNCOMMON, EYBCardTarget.Self)
            .SetSeries(CardSeries.OwariNoSeraph);
    public static final int INTELLECT_THRESHOLD = 6;
    private static final CardEffectChoice choices = new CardEffectChoice();

    public LestKarr()
    {
        super(DATA);

        Initialize(0, 0, 2, 3);
        SetUpgrade(0,0,0,1);
        SetAffinity_Dark(2,0,0);
        SetAffinity_Blue(1, 1, 0);
        SetAffinity_Orange(1);

        SetEthereal(true);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        final RandomizedList<AbstractCard> pool = GameUtilities.GetCardPoolInCombat(CardRarity.CURSE);
        pool.Add(new Frostbite());
        pool.Add(new SearingBurn());
        pool.Add(new Status_Burn());
        pool.Add(new Status_Wound());
        pool.Add(new Status_Slimed());
        pool.Add(new Crystallize());
        pool.Add(new VoidCard());
        pool.Add(new Dazed());
        final CardGroup choice = new CardGroup(CardGroup.CardGroupType.UNSPECIFIED);

        while (choice.size() < secondaryValue && pool.Size() > 0)
        {
            AbstractCard temp = pool.Retrieve(rng);
            if (!temp.tags.contains(AbstractCard.CardTags.HEALING) && !temp.tags.contains(GR.Enums.CardTags.VOLATILE)) {
                choice.addToTop(temp.makeCopy());
            }
        }

        GameActions.Bottom.SelectFromPile(name, 1, choice)
                .SetOptions(false, false)
                .AddCallback(cards ->
                {
                    for (AbstractCard c : cards) {
                        GameActions.Bottom.MakeCardInDrawPile(c).AddCallback(ca -> {
                            if (CheckPrimaryCondition(false)) {
                                GameActions.Bottom.ModifyTag(ca,HASTE, true);
                            }
                        });
                        GameActions.Bottom.Add(new RefreshHandLayout());
                    }

                    if (choices.TryInitialize(this))
                    {
                        choices.AddEffect(new GenericEffect_StackPower(PowerHelper.TemporaryFocus, GR.Tooltips.Focus, magicNumber, true));
                        choices.AddEffect(new GenericEffect_StackPower(PowerHelper.TemporaryDesecration, GR.Tooltips.Desecration, magicNumber, true));
                    }
                    choices.Select(1, m);
                });
    }

    @Override
    public boolean CheckPrimaryCondition(boolean tryUse)
    {
        return CombatStats.Affinities.GetPowerAmount(Affinity.Blue) >= INTELLECT_THRESHOLD;
    }
}