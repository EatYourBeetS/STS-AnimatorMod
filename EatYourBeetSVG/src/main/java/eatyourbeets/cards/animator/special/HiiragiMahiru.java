package eatyourbeets.cards.animator.special;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.CardSeries;
import eatyourbeets.cards.base.CardUseInfo;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.RandomizedList;

import java.util.Arrays;

public class HiiragiMahiru extends AnimatorCard {
    public static final AbstractCard[] forms = new AbstractCard[]{
      new HiiragiMahiru_Demon(),
      new HiiragiMahiru_Deva(),
      new HiiragiMahiru_Echo(),
      new HiiragiMahiru_Wraith()
    };

    public static final EYBCardData DATA = Register(HiiragiMahiru.class)
            .SetPower(4, CardRarity.RARE)
            .SetColor(CardColor.COLORLESS)
            .SetSeries(CardSeries.OwariNoSeraph)
            .PostInitialize(data ->
            {
                data.AddPreviews(Arrays.asList(forms), false);
            });

    public HiiragiMahiru() {
        super(DATA);

        Initialize(0, 0, 2);
        SetUpgrade(0, 0, 0);
        SetCostUpgrade(-1);

        SetEthereal(true);

        SetAffinity_Fire(2);
        SetAffinity_Poison(2);
        SetAffinity_Dark(2);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info) {
        RandomizedList<AbstractCard> formPowers = new RandomizedList<>();
        formPowers.AddAll(forms);

        CardGroup selection = new CardGroup(CardGroup.CardGroupType.UNSPECIFIED);

        for (int i=0; i<magicNumber; i++) {
            AbstractCard form = formPowers.Retrieve(rng).makeCopy();

            if (upgraded) {
                form.upgrade();
            }

            selection.addToBottom(form);
        }

        GameActions.Bottom.SelectFromPile(name, 1, selection)
        .SetOptions(false, false)
        .SetMessage(cardData.Strings.EXTENDED_DESCRIPTION[0])
        .AddCallback(cards ->
        {
            for (AbstractCard card : cards)
            {
                card.use(player, null);
            }
        });
    }
}