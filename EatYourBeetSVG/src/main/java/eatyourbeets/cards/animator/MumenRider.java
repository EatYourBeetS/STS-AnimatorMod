package eatyourbeets.cards.animator;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInHandAction;
import com.megacrit.cardcrawl.actions.common.PummelDamageAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.GameActionsHelper;
import eatyourbeets.cards.AnimatorCard;
import eatyourbeets.cards.Synergies;
import eatyourbeets.powers.PlayerStatistics;
import eatyourbeets.subscribers.OnEndOfTurnSubscriber;
import eatyourbeets.subscribers.OnStartOfTurnPostDrawSubscriber;
import patches.AbstractEnums;

public class MumenRider extends AnimatorCard implements OnStartOfTurnPostDrawSubscriber
{
    public static final String ID = CreateFullID(MumenRider.class.getSimpleName());

    private boolean firstTurn = true;

    public MumenRider()
    {
        super(ID, 0, CardType.ATTACK, CardColor.COLORLESS, CardRarity.UNCOMMON, CardTarget.ENEMY);

        Initialize(2, 0);

        this.purgeOnUse = true;

        SetSynergy(Synergies.OnePunchMan);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m)
    {
        if (upgraded)
        {
            GameActionsHelper.AddToBottom(new PummelDamageAction(m, new DamageInfo(p, 0, DamageInfo.DamageType.THORNS)));
            GameActionsHelper.AddToBottom(new PummelDamageAction(m, new DamageInfo(p, 0, DamageInfo.DamageType.THORNS)));
            GameActionsHelper.AddToBottom(new PummelDamageAction(m, new DamageInfo(p, 0, DamageInfo.DamageType.THORNS)));
            GameActionsHelper.DamageTarget(p, m, this, AbstractGameAction.AttackEffect.BLUNT_HEAVY);
        }
        else
        {
            GameActionsHelper.DamageTarget(p, m, this, AbstractGameAction.AttackEffect.SMASH);
        }

        GameActionsHelper.DrawCard(p, 1);

        this.purgeOnUse = true;

        if (!tags.contains(AbstractEnums.CardTags.TEMPORARY))
        {
            firstTurn = true;
            PlayerStatistics.onStartOfTurnPostDraw.Subscribe(this);
        }
    }

    @Override
    public void upgrade()
    {
        if (TryUpgrade())
        {
            upgradeDamage(1);
        }
    }

    @Override
    public void OnStartOfTurnPostDraw()
    {
        if (firstTurn)
        {
            firstTurn = false;
        }
        else
        {
            GameActionsHelper.AddToBottom(new MakeTempCardInHandAction(this));
            PlayerStatistics.onStartOfTurnPostDraw.Unsubscribe(this);
        }
    }
}