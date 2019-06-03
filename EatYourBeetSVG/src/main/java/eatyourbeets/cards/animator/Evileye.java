package eatyourbeets.cards.animator;

import basemod.BaseMod;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.GameActionsHelper;
import eatyourbeets.Utilities;
import eatyourbeets.actions.VariableDiscardAction;
import eatyourbeets.cards.AnimatorCard;
import eatyourbeets.cards.Synergies;

import java.util.ArrayList;

public class Evileye extends AnimatorCard
{
    public static final String ID = CreateFullID(Evileye.class.getSimpleName());

    public Evileye()
    {
        super(ID, 1, CardType.ATTACK, CardRarity.COMMON, CardTarget.SELF_AND_ENEMY);

        Initialize(4,0);

        SetSynergy(Synergies.Overlord);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m)
    {
        CardCrawlGame.sound.play("ORB_FROST_CHANNEL", 0.2F);
        GameActionsHelper.DamageTarget(p, m, this.damage, this.damageTypeForTurn, AbstractGameAction.AttackEffect.SLASH_DIAGONAL);
        CardCrawlGame.sound.play("ORB_FROST_Evoke", 0.2F);

        GameActionsHelper.DrawCard(p, 1);
        GameActionsHelper.AddToBottom(new VariableDiscardAction(p, BaseMod.MAX_HAND_SIZE, m, this::OnDiscard));
    }

    @Override
    public void upgrade() 
    {
        if (TryUpgrade())
        {
            upgradeDamage(3);
        }
    }

    private void OnDiscard(Object state, ArrayList<AbstractCard> discarded)
    {
        AbstractMonster m = Utilities.SafeCast(state, AbstractMonster.class);
        if (state == null || discarded == null)
        {
            return;
        }

        AbstractPlayer p = AbstractDungeon.player;
        if (p.orbs.size() > 0)
        {
            for (int i = 0; i < discarded.size(); i++)
            {
                p.orbs.get(0).onStartOfTurn();
                p.orbs.get(0).onEndOfTurn();
            }
        }
    }
}