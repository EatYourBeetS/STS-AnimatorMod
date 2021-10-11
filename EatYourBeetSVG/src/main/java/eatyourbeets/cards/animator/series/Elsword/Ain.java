package eatyourbeets.cards.animator.series.Elsword;

import com.megacrit.cardcrawl.actions.GameActionManager;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.monsters.MonsterGroup;
import com.megacrit.cardcrawl.vfx.combat.FallingIceEffect;
import eatyourbeets.cards.animator.tokens.AffinityToken;
import eatyourbeets.cards.base.*;
import eatyourbeets.cards.base.attributes.AbstractAttribute;
import eatyourbeets.effects.AttackEffects;
import eatyourbeets.stances.IntellectStance;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameEffects;

public class Ain extends AnimatorCard
{
    public static final EYBCardData DATA = Register(Ain.class)
            .SetAttack(2, CardRarity.UNCOMMON, EYBAttackType.Elemental, EYBCardTarget.ALL)
            .SetSeries(CardSeries.Elsword)
            .PostInitialize(data -> data.AddPreview(AffinityToken.GetCard(Affinity.Blue), true));

    public Ain()
    {
        super(DATA);

        Initialize(2, 0, 3, 3);
        SetUpgrade(1, 0, 0, 0);

        SetAffinity_Light(1);
        SetAffinity_Blue(2, 0, 1);

        SetAffinityRequirement(Affinity.Light, 3);
    }

    @Override
    protected void OnUpgrade()
    {
        upgradedDamage = true;
    }

    @Override
    public AbstractAttribute GetDamageInfo()
    {
        return super.GetDamageInfo().AddMultiplier(magicNumber);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        //GameActions.Bottom.VFX(new BlizzardEffect(magicNumber, AbstractDungeon.getMonsters().shouldFlipVfx()), 0.6f);
        GameActions.Bottom.Callback(() ->
        {
            MonsterGroup monsters = AbstractDungeon.getMonsters();
            int frostCount = monsters.monsters.size() + magicNumber + 5;

            CardCrawlGame.sound.playA("ORB_FROST_CHANNEL", -0.25f - (float)frostCount / 200f);
            for (int i = 0; i < frostCount; i++)
            {
                GameEffects.Queue.Add(new FallingIceEffect(frostCount, monsters.shouldFlipVfx()));
            }
        });

        for (int i = 0; i < magicNumber; i++)
        {
            GameActions.Bottom.DealDamageToAll(this, AttackEffects.NONE).SetVFX(true, false);
        }

        if (CheckSpecialCondition(true))
        {
            GameActions.Bottom.ChangeStance(IntellectStance.STANCE_ID);
        }

        if (TrySpendAffinity(Affinity.Light))
        {
            GameActions.Bottom.MakeCardInHand(AffinityToken.GetCopy(Affinity.Blue, upgraded));
        }
    }

    @Override
    public boolean CheckSpecialCondition(boolean tryUse)
    {
        return GameActionManager.totalDiscardedThisTurn > 0;
    }
}