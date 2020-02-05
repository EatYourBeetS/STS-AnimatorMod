package eatyourbeets.cards.animator.colorless.rare;

import com.evacipated.cardcrawl.mod.stslib.cards.interfaces.StartupCard;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.monsters.MonsterGroup;
import com.megacrit.cardcrawl.orbs.AbstractOrb;
import com.megacrit.cardcrawl.vfx.combat.FallingIceEffect;
import com.megacrit.cardcrawl.vfx.combat.FireballEffect;
import com.megacrit.cardcrawl.vfx.combat.LightningEffect;
import com.megacrit.cardcrawl.vfx.combat.WhirlwindEffect;
import eatyourbeets.actions.damage.DealDamageToRandomEnemy;
import eatyourbeets.cards.animator.special.OrbCore;
import eatyourbeets.cards.base.*;
import eatyourbeets.cards.base.attributes.AbstractAttribute;
import eatyourbeets.interfaces.csharp.ActionT0;
import eatyourbeets.interfaces.markers.Spellcaster;
import eatyourbeets.ui.EffectHistory;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameEffects;
import eatyourbeets.utilities.JavaUtilities;
import eatyourbeets.utilities.RandomizedList;

import java.util.ArrayList;
import java.util.HashSet;

public class Patchouli extends AnimatorCard implements Spellcaster, StartupCard
{
    public static final EYBCardData DATA = Register(Patchouli.class).SetAttack(3, CardRarity.RARE, EYBAttackType.Elemental, EYBCardTarget.Random).SetColor(CardColor.COLORLESS);

    private int cachedOrbAmount;

    public Patchouli()
    {
        super(DATA);

        Initialize(8, 0, 0, 2);
        SetUpgrade(3, 0, 0, 0);
        SetScaling(2, 0, 0);

        SetAttackType(EYBAttackType.Elemental);
        SetSynergy(Synergies.TouhouProject);
    }

    @Override
    public AbstractAttribute GetDamageInfo()
    {
        return super.GetDamageInfo().AddMultiplier(1 + magicNumber);
    }

    @Override
    public void Refresh(AbstractMonster enemy)
    {
        super.Refresh(enemy);

        ArrayList<AbstractOrb> orbs = AbstractDungeon.actionManager.orbsChanneledThisCombat;
        if (cachedOrbAmount != orbs.size())
        {
            HashSet<String> uniqueOrbs = new HashSet<>();
            for (AbstractOrb orb : orbs)
            {
                uniqueOrbs.add(orb.ID);
            }

            magicNumber = uniqueOrbs.size();
            isMagicNumberModified = (magicNumber != baseMagicNumber);
            cachedOrbAmount = orbs.size();
        }
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m)
    {
        if (magicNumber > 0)
        {
            RandomizedList<ActionT0> actions = new RandomizedList<>();

            for (int i = 0; i < magicNumber; i++)
            {
                if (actions.Count() == 0)
                {
                    actions.Add(this::Aether);
                    actions.Add(this::Fire);
                    actions.Add(this::Frost);
                    actions.Add(this::Lightning);
                }

                actions.Retrieve(AbstractDungeon.cardRandomRng).Invoke();
                GameActions.Bottom.WaitRealtime(0.2f);
            }
        }
    }

    private void Lightning()
    {
        CreateDamageAction().SetDamageEffect(e ->
        {
            CardCrawlGame.sound.play("ORB_LIGHTNING_EVOKE", 0.2f);
            GameEffects.Queue.Add(new LightningEffect(e.drawX, e.drawY));
        });
    }

    private void Frost()
    {
        CreateDamageAction().SetDamageEffect(e ->
        {
            MonsterGroup monsters = AbstractDungeon.getMonsters();
            int frostCount = monsters.monsters.size() + 5;

            CardCrawlGame.sound.playA("ORB_FROST_CHANNEL", -0.25F - (float) frostCount / 200.0F);
            for (int f = 0; f < frostCount; f++)
            {
                GameEffects.Queue.Add(new FallingIceEffect(frostCount, monsters.shouldFlipVfx()));
            }
        });
    }

    private void Aether()
    {
        CreateDamageAction().SetDamageEffect(__ ->
        {
            CardCrawlGame.sound.play("ATTACK_WHIRLWIND", 0.2f);
            GameEffects.Queue.Add(new WhirlwindEffect());
        });
    }

    private void Fire()
    {
        CreateDamageAction().SetDamageEffect(e ->
        {
            CardCrawlGame.sound.play("ATTACK_FIRE", 0.2f);
            GameEffects.Queue.Add(new FireballEffect(AbstractDungeon.player.hb.cX, AbstractDungeon.player.hb.cY, e.hb.cX, e.hb.cY));
        });
    }

    private DealDamageToRandomEnemy CreateDamageAction()
    {
        return GameActions.Bottom.DealDamageToRandomEnemy(baseDamage, damageTypeForTurn, AbstractGameAction.AttackEffect.NONE).SetOptions(true, false);
    }

    @Override
    public boolean atBattleStartPreDraw()
    {
        if (EffectHistory.TryActivateLimited(cardID))
        {
            GameActions.Bottom.Wait(0.3f);
            GameActions.Bottom.MakeCardInDiscardPile(JavaUtilities.GetRandomElement(OrbCore.GetAllCores()).makeCopy());

            return true;
        }

        return false;
    }
}