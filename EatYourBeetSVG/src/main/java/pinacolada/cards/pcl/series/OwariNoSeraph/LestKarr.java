package pinacolada.cards.pcl.series.OwariNoSeraph;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.actions.special.RefreshHandLayout;
import eatyourbeets.utilities.RandomizedList;
import pinacolada.cards.base.*;
import pinacolada.cards.pcl.status.*;
import pinacolada.misc.GenericEffects.GenericEffect_StackPower;
import pinacolada.powers.PCLPowerHelper;
import pinacolada.resources.GR;
import pinacolada.utilities.PCLActions;
import pinacolada.utilities.PCLGameUtilities;

public class LestKarr extends PCLCard
{
    public static final PCLCardData DATA = Register(LestKarr.class)
            .SetSkill(0, CardRarity.UNCOMMON, eatyourbeets.cards.base.EYBCardTarget.Self)
            .SetSeries(CardSeries.OwariNoSeraph);
    public static final int INTELLECT_THRESHOLD = 5;
    private static final CardEffectChoice choices = new CardEffectChoice();

    public LestKarr()
    {
        super(DATA);

        Initialize(0, 0, 2, 3);
        SetUpgrade(0,0,0,1);
        SetAffinity_Dark(1,0,0);
        SetAffinity_Blue(1, 0, 0);
        SetAffinity_Orange(1);

        SetEthereal(true);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        final RandomizedList<AbstractCard> pool = PCLGameUtilities.GetCardPoolInCombat(CardRarity.CURSE);
        pool.Add(new Status_Frostbite());
        pool.Add(new Status_Burn());
        pool.Add(new Status_Wound());
        pool.Add(new Status_Slimed());
        pool.Add(new Crystallize());
        pool.Add(new Status_Void());
        pool.Add(new Status_Dazed());
        final CardGroup choice = new CardGroup(CardGroup.CardGroupType.UNSPECIFIED);

        while (choice.size() < secondaryValue && pool.Size() > 0)
        {
            AbstractCard temp = pool.Retrieve(rng);
            if (!temp.tags.contains(AbstractCard.CardTags.HEALING) && !temp.tags.contains(GR.Enums.CardTags.VOLATILE)) {
                choice.addToTop(temp.makeCopy());
            }
        }

        PCLActions.Bottom.SelectFromPile(name, 1, choice)
                .SetOptions(false, false)
                .AddCallback(cards ->
                {
                    for (AbstractCard c : cards) {
                        PCLActions.Bottom.MakeCardInDrawPile(c).AddCallback(ca -> {
                            if (info.IsSynergizing) {
                                PCLActions.Bottom.ModifyTag(ca,HASTE, true);
                            }
                        });
                        PCLActions.Bottom.Add(new RefreshHandLayout());
                    }

                    if (choices.TryInitialize(this))
                    {
                        choices.AddEffect(new GenericEffect_StackPower(PCLPowerHelper.TemporaryDexterity, GR.Tooltips.Dexterity, magicNumber, true));
                        choices.AddEffect(new GenericEffect_StackPower(PCLPowerHelper.TemporaryFocus, GR.Tooltips.Focus, magicNumber, true));
                    }
                    choices.Select(1, m);
                });
    }
}