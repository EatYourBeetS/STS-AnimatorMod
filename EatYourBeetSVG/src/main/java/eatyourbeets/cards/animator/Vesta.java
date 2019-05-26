package eatyourbeets.cards.animator;

import com.megacrit.cardcrawl.actions.common.MakeTempCardInHandAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.vfx.cardManip.ShowCardBrieflyEffect;
import eatyourbeets.GameActionsHelper;
import eatyourbeets.actions.ChooseFromPileAction;
import eatyourbeets.cards.AnimatorCard;
import eatyourbeets.cards.Synergies;
import eatyourbeets.powers.PlayerStatistics;
import eatyourbeets.subscribers.OnStartOfTurnPostDrawSubscriber;

import java.util.ArrayList;

public class Vesta extends AnimatorCard implements OnStartOfTurnPostDrawSubscriber
{
    public static final String ID = CreateFullID(Vesta.class.getSimpleName());

    private int timer;
    private AbstractCard toRetrieve;

    public Vesta()
    {
        super(ID, 1, CardType.SKILL, CardRarity.UNCOMMON, CardTarget.SELF);

        Initialize(0,0, 3);

        this.exhaust = true;

        AddExtendedDescription();

        SetSynergy(Synergies.TenSura);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) 
    {
        GameActionsHelper.AddToBottom(new ChooseFromPileAction(1, false, Vesta_Elixir.GetCardGroup(), this::OnSelected,this, "", true));
    }

    @Override
    public void upgrade()
    {
        if (TryUpgrade())
        {
            upgradeMagicNumber(-1);
        }
    }

    protected void OnSelected(Object state, ArrayList<AbstractCard> cards)
    {
        if (state == this && cards.size() == 1)
        {
            toRetrieve = cards.get(0).makeStatEquivalentCopy();
            timer = magicNumber;
            PlayerStatistics.onStartOfTurnPostDraw.Subscribe(this);
        }
    }

    @Override
    public void OnStartOfTurnPostDraw()
    {
        if (timer > 0)
        {
            timer -= 1;
        }
        else
        {
            AbstractDungeon.effectsQueue.add(new ShowCardBrieflyEffect(this.makeStatEquivalentCopy()));

            toRetrieve.upgrade();
            GameActionsHelper.AddToBottom(new MakeTempCardInHandAction(toRetrieve));

            PlayerStatistics.onStartOfTurnPostDraw.Unsubscribe(this);
        }
    }
}