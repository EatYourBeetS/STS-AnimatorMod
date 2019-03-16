package eatyourbeets.cards.animator;

import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInHandAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.colorless.MasterOfStrategy;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.vfx.BorderFlashEffect;
import eatyourbeets.GameActionsHelper;
import eatyourbeets.actions.WaitRealtimeAction;
import eatyourbeets.cards.AnimatorCard;
import eatyourbeets.cards.Synergies;
import eatyourbeets.powers.PlayerStatistics;
import eatyourbeets.powers.ShiroPower;

import java.util.ArrayList;

public class Shiro extends AnimatorCard
{
    public static final String ID = CreateFullID(Shiro.class.getSimpleName());

    public Shiro()
    {
        super(ID, 4, CardType.POWER, CardRarity.RARE, CardTarget.SELF);

        Initialize(0,0);

        SetSynergy(Synergies.NoGameNoLife);
    }

    @Override
    public void applyPowers()
    {
        super.applyPowers();

        if (!this.freeToPlayOnce)
        {
            this.setCostForTurn(this.cost - PlayerStatistics.getSynergiesThisTurn());
        }
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) 
    {
        ArrayList<AbstractCard> cards = AbstractDungeon.actionManager.cardsPlayedThisTurn;
        if (cards.size() > 1 && cards.get(cards.size() - 2).cardID.equals(Sora.ID))
        {
            GameActionsHelper.AddToBottom(new VFXAction(new BorderFlashEffect(Color.PINK)));
            GameActionsHelper.AddToBottom(new MakeTempCardInHandAction(new MasterOfStrategy()));
            GameActionsHelper.AddToBottom(new WaitRealtimeAction(0.6f));
        }

        GameActionsHelper.ApplyPower(p, p, new ShiroPower(p), 1);
    }

    @Override
    public void upgrade() 
    {
        if (TryUpgrade())
        {
            upgradeBaseCost(3);
        }
    }
}