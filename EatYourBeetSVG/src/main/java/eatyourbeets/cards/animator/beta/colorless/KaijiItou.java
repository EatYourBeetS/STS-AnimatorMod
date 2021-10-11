package eatyourbeets.cards.animator.beta.colorless;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.actions.special.RefreshHandLayout;
import eatyourbeets.cards.animator.beta.special.KaijiItou_RestrictedPaper;
import eatyourbeets.cards.animator.beta.special.KaijiItou_RestrictedRock;
import eatyourbeets.cards.animator.beta.special.KaijiItou_RestrictedScissors;
import eatyourbeets.cards.base.*;
import eatyourbeets.utilities.GameActions;

public class KaijiItou extends AnimatorCard
{
    public static final EYBCardData DATA = Register(KaijiItou.class).SetSkill(1, CardRarity.UNCOMMON, EYBCardTarget.None).SetMaxCopies(2).SetColor(CardColor.COLORLESS).SetSeries(CardSeries.Kaiji)
            .PostInitialize(data -> {
                data.AddPreview(new KaijiItou_RestrictedPaper(), false);
                data.AddPreview(new KaijiItou_RestrictedRock(), false);
                data.AddPreview(new KaijiItou_RestrictedScissors(), false);
            });;

    public KaijiItou()
    {
        super(DATA);

        Initialize(0, 0, 1, 0);
        SetExhaust(true);

        SetAffinity_Earth(2);

        SetAffinityRequirement(Affinity.General, 4);
    }

    public void OnUpgrade() {
        SetRetain(true);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info) {
        GameActions.Bottom.Draw(magicNumber);
    }

    @Override
    public void OnLateUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info) {
        AbstractCard paper = new KaijiItou_RestrictedPaper();
        AbstractCard rock = new KaijiItou_RestrictedRock();
        AbstractCard scissors = new KaijiItou_RestrictedScissors();

        GameActions.Bottom.MakeCardInDiscardPile(paper).SetUpgrade(upgraded, false);
        GameActions.Bottom.MakeCardInDiscardPile(rock).SetUpgrade(upgraded, false);
        GameActions.Bottom.MakeCardInDiscardPile(scissors).SetUpgrade(upgraded, false);

        GameActions.Last.Callback(() -> {
            if (CheckAffinity(Affinity.General)) {
                GameActions.Bottom.FetchFromPile(name, 1, p.discardPile)
                        .SetFilter(c -> c.uuid.equals(paper.uuid) || c.uuid.equals(rock.uuid) || c.uuid.equals(scissors.uuid))
                        .SetOptions(false, true)
                        .AddCallback(cards ->
                        {
                            if (cards.size() > 0)
                            {
                                AbstractCard card = cards.get(0);
                                GameActions.Bottom.Add(new RefreshHandLayout());
                            }
                        });
            }
        });
    }
}