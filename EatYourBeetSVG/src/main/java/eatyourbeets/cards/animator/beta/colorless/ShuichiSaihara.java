package eatyourbeets.cards.animator.beta.colorless;

import com.evacipated.cardcrawl.mod.stslib.actions.common.MoveCardsAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.actions.special.RefreshHandLayout;
import eatyourbeets.cards.animator.beta.special.KaedeAkamatsu;
import eatyourbeets.cards.base.*;
import eatyourbeets.powers.AnimatorPower;
import eatyourbeets.resources.GR;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameUtilities;
import eatyourbeets.utilities.RandomizedList;

public class ShuichiSaihara extends AnimatorCard
{
    public static final EYBCardData DATA = Register(ShuichiSaihara.class).SetSkill(0, CardRarity.UNCOMMON, EYBCardTarget.None).SetColor(CardColor.COLORLESS).SetSeries(CardSeries.Danganronpa)
            .PostInitialize(data -> data.AddPreview(new KaedeAkamatsu(), false));;

    public ShuichiSaihara()
    {
        super(DATA);

        Initialize(0, 0, 0, 0);
        SetExhaust(true);

        SetAffinity_Blue(1);
        SetProtagonist(true);
        SetHarmonic(true);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        CardGroup[] groups = upgraded ? (new CardGroup[] {p.discardPile, p.drawPile}) : (new CardGroup[] {p.discardPile});
        GameActions.Bottom.FetchFromPile(name, 1, groups)
                .SetMessage(MoveCardsAction.TEXT[0])
                .SetOptions(false, false)
                .AddCallback(cards ->
                {
                    if (cards.size() > 0)
                    {
                        AbstractCard card = cards.get(0);
                        card.exhaust = true;
                        GameActions.Bottom.Add(new RefreshHandLayout());
                        GameActions.Bottom.StackPower(new ShuichiSaiharaPower(p, card));

                        if (GameUtilities.IsHindrance(card) && info.TryActivateLimited()) {
                            GameActions.Bottom.MakeCardInHand(new KaedeAkamatsu());
                        }
                    }
                });
    }

    public static class ShuichiSaiharaPower extends AnimatorPower
    {
        private AbstractCard card;
        public ShuichiSaiharaPower(AbstractPlayer owner, AbstractCard card)
        {
            super(owner, ShuichiSaihara.DATA);

            this.card = card;

            updateDescription();
        }

        @Override
        public void onExhaust(AbstractCard card)
        {
            super.onExhaust(card);

            if (card.uuid.equals(this.card.uuid)) {

                CardRarity r;
                switch (card.rarity) {
                    case UNCOMMON:
                        r = CardRarity.RARE;
                        break;
                    case SPECIAL:
                    case COMMON:
                        r = CardRarity.UNCOMMON;
                        break;
                    default:
                        r = CardRarity.COMMON;
                }
                final RandomizedList<AbstractCard> pool = GameUtilities.GetCardPoolInCombat(r);
                final CardGroup choice = new CardGroup(CardGroup.CardGroupType.UNSPECIFIED);

                while (choice.size() < 3 && pool.Size() > 0)
                {
                    AbstractCard temp = pool.Retrieve(rng);
                    if (!(temp.cardID.equals(card.cardID) || temp.tags.contains(AbstractCard.CardTags.HEALING) || temp.tags.contains(GR.Enums.CardTags.VOLATILE))) {
                        choice.addToTop(temp.makeCopy());
                    }
                }

                GameActions.Bottom.SelectFromPile(name, 1, choice)
                        .SetOptions(false, false)
                        .AddCallback(cards ->
                        {
                            GameActions.Bottom.MakeCardInHand(cards.get(0));
                            GameActions.Bottom.Add(new RefreshHandLayout());
                        });
                flashWithoutSound();
                this.RemovePower();
            }
        }



        @Override
        public void updateDescription()
        {
            description = FormatDescription(0, card.name.replace(" ", " #y"));
        }
    }
}