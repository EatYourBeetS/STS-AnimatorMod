package eatyourbeets.cards.animator;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.orbs.Dark;
import com.megacrit.cardcrawl.vfx.cardManip.ShowCardBrieflyEffect;
import eatyourbeets.GameActionsHelper;
import eatyourbeets.cards.AnimatorCard;
import eatyourbeets.cards.Synergies;
import eatyourbeets.powers.PlayerStatistics;
import eatyourbeets.subscribers.OnStartOfTurnPostDrawSubscriber;

public class Urushihara extends AnimatorCard implements OnStartOfTurnPostDrawSubscriber
{
    public static final String ID = CreateFullID(Urushihara.class.getSimpleName());

    private int lazyCounter;

    public Urushihara()
    {
        super(ID, 1, CardType.ATTACK, CardColor.COLORLESS, CardRarity.UNCOMMON, CardTarget.ALL);

        Initialize(21, 0);

        this.isMultiDamage = true;
        this.lazyCounter = 0;

        SetSynergy(Synergies.HatarakuMaouSama);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m)
    {
        Urushihara other = (Urushihara) makeStatEquivalentCopy();

        other.lazyCounter = AbstractDungeon.miscRng.random(3);
        if (!upgraded)
        {
            other.lazyCounter += 1;
        }

        GameActionsHelper.ChannelOrb(new Dark(), true);

        PlayerStatistics.onStartOfTurnPostDraw.Subscribe(other);
    }

    @Override
    public void upgrade()
    {
        TryUpgrade();
    }

    @Override
    public void OnStartOfTurnPostDraw()
    {
        if (lazyCounter > 0)
        {
            lazyCounter -= 1;
        }
        else
        {
            applyPowers();
            AbstractDungeon.effectsQueue.add(new ShowCardBrieflyEffect(this));
            GameActionsHelper.DamageAllEnemies(AbstractDungeon.player, this.multiDamage, DamageInfo.DamageType.THORNS, AbstractGameAction.AttackEffect.FIRE);

            PlayerStatistics.onStartOfTurnPostDraw.Unsubscribe(this);
            PlayerStatistics.UsePenNib();
        }
    }
}