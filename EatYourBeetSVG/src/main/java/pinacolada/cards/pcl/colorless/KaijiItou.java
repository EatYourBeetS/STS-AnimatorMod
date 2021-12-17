package pinacolada.cards.pcl.colorless;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.actions.special.RefreshHandLayout;
import pinacolada.cards.base.*;
import pinacolada.cards.pcl.special.KaijiItou_RestrictedPaper;
import pinacolada.cards.pcl.special.KaijiItou_RestrictedRock;
import pinacolada.cards.pcl.special.KaijiItou_RestrictedScissors;
import pinacolada.utilities.PCLActions;

public class KaijiItou extends PCLCard
{
    public static final PCLCardData DATA = Register(KaijiItou.class).SetSkill(1, CardRarity.UNCOMMON, eatyourbeets.cards.base.EYBCardTarget.None).SetMaxCopies(2).SetColor(CardColor.COLORLESS).SetSeries(CardSeries.Kaiji)
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

        SetAffinity_Orange(1);

        SetAffinityRequirement(PCLAffinity.General, 8);
    }

    public void OnUpgrade() {
        SetRetain(true);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info) {
        PCLActions.Bottom.Draw(magicNumber);
    }

    @Override
    public void OnLateUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info) {
        AbstractCard paper = new KaijiItou_RestrictedPaper();
        AbstractCard rock = new KaijiItou_RestrictedRock();
        AbstractCard scissors = new KaijiItou_RestrictedScissors();

        PCLActions.Bottom.MakeCardInDiscardPile(paper).SetUpgrade(upgraded, false);
        PCLActions.Bottom.MakeCardInDiscardPile(rock).SetUpgrade(upgraded, false);
        PCLActions.Bottom.MakeCardInDiscardPile(scissors).SetUpgrade(upgraded, false);

        PCLActions.Bottom.TryChooseSpendAffinity(this).AddConditionalCallback(() -> {
            PCLActions.Bottom.FetchFromPile(name, 1, p.discardPile)
                    .SetFilter(c -> c.uuid.equals(paper.uuid) || c.uuid.equals(rock.uuid) || c.uuid.equals(scissors.uuid))
                    .SetOptions(false, true)
                    .AddCallback(cards ->
                    {
                        if (cards.size() > 0)
                        {
                            AbstractCard card = cards.get(0);
                            PCLActions.Bottom.Add(new RefreshHandLayout());
                        }
                    });
        });
    }
}