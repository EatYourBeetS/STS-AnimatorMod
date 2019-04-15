package eatyourbeets.cards.animator;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.AnimatorCard;
import eatyourbeets.cards.Synergies;
import eatyourbeets.powers.PlayerStatistics;
import eatyourbeets.subscribers.OnAfterCardPlayedSubscriber;
import eatyourbeets.subscribers.OnBattleStartSubscriber;

public class Rimuru extends AnimatorCard implements OnBattleStartSubscriber, OnAfterCardPlayedSubscriber
{
    public static final String ID = CreateFullID(Rimuru.class.getSimpleName());

    private AbstractCard copy;

    public Rimuru()
    {
        super(ID, -2, CardType.SKILL, CardRarity.RARE, CardTarget.ALL);

        Initialize(0, 0);

        this.copy = this;

        if (PlayerStatistics.InBattle() && !CardCrawlGame.isPopupOpen)
        {
            OnBattleStart();
        }

        SetSynergy(Synergies.TenSura, true);
    }

    @Override
    public void OnBattleStart()
    {
        PlayerStatistics.onAfterCardPlayed.Subscribe(this);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m)
    {
        //GameActionsHelper.AddToBottom(new MakeTempCardInHandAction(new HigakiRinne()));
    }

    @Override
    public void upgrade()
    {
        if (TryUpgrade())
        {
            this.retain = true;
        }
    }

    private boolean transform(CardGroup group, AbstractCard card)
    {
        int index = group.group.indexOf(copy);
        if (index >= 0)
        {
            AbstractCard newCopy = card.makeStatEquivalentCopy();

            group.group.remove(index);
            group.group.add(index, newCopy);

            if (this.upgraded)
            {
                newCopy.retain = true;
            }

            newCopy.name = this.name;

            if (group.type == CardGroup.CardGroupType.HAND)
            {
                newCopy.current_x = copy.current_x;
                newCopy.current_y = copy.current_y;
                newCopy.target_x = copy.target_x;
                newCopy.target_y = copy.target_y;
            }

            this.copy = newCopy;
            this.copy.applyPowers();

            return true;
        }

        return false;
    }

    @Override
    public void OnAfterCardPlayed(AbstractCard card)
    {
        if (card == copy || card instanceof Rimuru)
        {
            return;
        }

        AbstractPlayer p = AbstractDungeon.player;
        if (!transform(p.hand, card))
        {
            if (!transform(p.drawPile, card))
            {
                if (!transform(p.discardPile, card))
                {
                    if (!transform(p.exhaustPile, card))
                    {
                        PlayerStatistics.onAfterCardPlayed.Unsubscribe(this);
                    }
                }
            }
        }
    }

    public void ShutUpJava()
    {
        transform(AbstractDungeon.player.limbo, null);
    }
}