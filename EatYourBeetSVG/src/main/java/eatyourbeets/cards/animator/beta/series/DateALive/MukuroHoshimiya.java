package eatyourbeets.cards.animator.beta.series.DateALive;

import com.evacipated.cardcrawl.mod.stslib.cards.interfaces.StartupCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.EYBAttackType;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.effects.AttackEffects;
import eatyourbeets.interfaces.subscribers.OnAddedToDrawPileSubscriber;
import eatyourbeets.interfaces.subscribers.OnShuffleSubscriber;
import eatyourbeets.powers.CombatStats;
import eatyourbeets.utilities.CardSelection;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.JUtils;

public class MukuroHoshimiya extends AnimatorCard implements StartupCard, OnShuffleSubscriber, OnAddedToDrawPileSubscriber
{
    public static final EYBCardData DATA = Register(MukuroHoshimiya.class).SetAttack(2, CardRarity.RARE, EYBAttackType.Elemental).SetSeriesFromClassPackage();

    public MukuroHoshimiya()
    {
        super(DATA);

        Initialize(16, 0, 4);
        SetUpgrade(0,0,-1);
        SetAffinity_Blue(2, 0, 0);
        SetAffinity_Orange(1, 0, 0);
    }

    @Override
    protected float GetInitialDamage()
    {
        return baseDamage + (player.drawPile.size() / magicNumber);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, boolean isSynergizing)
    {
        GameActions.Bottom.DealDamage(this, m, AttackEffects.PSYCHOKINESIS);
    }

    @Override
    public boolean atBattleStartPreDraw()
    {
        OnShuffle(false);

        return false;
    }

    @Override
    public void triggerWhenCreated(boolean startOfBattle)
    {
        super.triggerWhenCreated(startOfBattle);

        CombatStats.onShuffle.Subscribe(this);
    }

    @Override
    public void OnAddedToDrawPile(boolean visualOnly, CardSelection.Mode destination)
    {
        OnShuffle(false);
    }

    @Override
    public void OnShuffle(boolean triggerRelics)
    {
        GameActions.Top.Callback(() -> JUtils.ChangeIndex(this, player.drawPile.group, player.drawPile.size() - 6));
    }
}